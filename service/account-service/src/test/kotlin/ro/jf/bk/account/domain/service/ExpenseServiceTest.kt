package ro.jf.bk.account.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ro.jf.bk.account.domain.command.CreateBudgetCommand
import ro.jf.bk.account.domain.command.DeleteBudgetCommand
import ro.jf.bk.account.domain.command.UpdateBudgetCommand
import ro.jf.bk.account.domain.command.UpdateBudgetConfigurationCommand
import ro.jf.bk.account.domain.error.BUDGET_PERCENT_SUM_NOT_100
import ro.jf.bk.account.domain.error.BudgetException
import ro.jf.bk.account.domain.model.Budget
import ro.jf.bk.account.domain.model.Budget.Percent
import ro.jf.bk.account.domain.model.BudgetConfiguration
import ro.jf.bk.account.domain.model.BudgetConfiguration.BudgetVersion
import java.time.Month
import java.time.YearMonth
import java.util.*
import java.util.UUID.randomUUID

class ExpenseServiceTest {
    private val budgetRepository = mock<BudgetRepository>()
    private val expenseService = ExpenseService(budgetRepository)

    @Test
    fun `should update budget configuration when initial setup`() {
        val userId = randomUUID()
        val targetIds = (1..5).map { randomUUID() }
        val availableFrom = YearMonth.of(2021, Month.JANUARY)
        val command = UpdateBudgetConfigurationCommand(
            availableFrom = availableFrom,
            budgets = listOf(
                CreateBudgetCommand("Needs", Percent(50), listOf(targetIds[0], targetIds[1])),
                CreateBudgetCommand("Wants", Percent(30), listOf(targetIds[2], targetIds[3])),
                CreateBudgetCommand("Savings", Percent(20), listOf(targetIds[4])),
            )
        )
        val budgetId1 = randomUUID()
        val budgetId2 = randomUUID()
        val budgetId3 = randomUUID()
        val budget1Version = Budget.Version(availableFrom, Percent(50), listOf(targetIds[0], targetIds[1]))
        val budget2Version = Budget.Version(availableFrom, Percent(30), listOf(targetIds[2], targetIds[3]))
        val budget3Version = Budget.Version(availableFrom, Percent(20), listOf(targetIds[4]))
        whenever(budgetRepository.findAll(userId))
            .thenReturn(emptyList())
            .thenReturn(
                listOf(
                    Budget(budgetId1, userId, "Needs", listOf(budget1Version)),
                    Budget(budgetId2, userId, "Wants", listOf(budget2Version)),
                    Budget(budgetId3, userId, "Savings", listOf(budget3Version)),
                )
            )

        val budgetConfiguration = expenseService.updateBudgetConfiguration(userId, command)

        assertThat(budgetConfiguration).isEqualTo(
            BudgetConfiguration(
                userId,
                command.availableFrom,
                listOf(
                    BudgetVersion(budgetId1, "Needs", listOf(targetIds[0], targetIds[1]), Percent(50)),
                    BudgetVersion(budgetId2, "Wants", listOf(targetIds[2], targetIds[3]), Percent(30)),
                    BudgetVersion(budgetId3, "Savings", listOf(targetIds[4]), Percent(20)),
                )
            )
        )

        verify(budgetRepository).batchUpdate(userId, command.availableFrom, command.budgets)
    }

    @Test
    fun `should deny update of budget configuration when initial setup and budget percentages do not add up to 100`() {
        val userId = randomUUID()
        val targetIds = (1..5).map { randomUUID() }
        val availableFrom = YearMonth.of(2024, Month.JANUARY)
        val command = UpdateBudgetConfigurationCommand(
            availableFrom = availableFrom,
            budgets = listOf(
                CreateBudgetCommand("Needs", Percent(50), listOf(targetIds[0], targetIds[1])),
                CreateBudgetCommand("Wants", Percent(30), listOf(targetIds[2], targetIds[3])),
                CreateBudgetCommand("Savings", Percent(10), listOf(targetIds[4])),
            )
        )
        val budgetId1 = randomUUID()
        val budgetId2 = randomUUID()
        val budgetId3 = randomUUID()
        val budget1Version = Budget.Version(availableFrom, Percent(50), listOf(targetIds[0], targetIds[1]))
        val budget2Version = Budget.Version(availableFrom, Percent(30), listOf(targetIds[2], targetIds[3]))
        val budget3Version = Budget.Version(availableFrom, Percent(20), listOf(targetIds[4]))
        whenever(budgetRepository.findAll(userId))
            .thenReturn(emptyList())
            .thenReturn(
                listOf(
                    Budget(budgetId1, userId, "Needs", listOf(budget1Version)),
                    Budget(budgetId2, userId, "Wants", listOf(budget2Version)),
                    Budget(budgetId3, userId, "Savings", listOf(budget3Version)),
                )
            )

        assertThatThrownBy { expenseService.updateBudgetConfiguration(userId, command) }
            .isInstanceOf(BudgetException::class.java)
            .hasMessage(BUDGET_PERCENT_SUM_NOT_100)

        verify(budgetRepository, never()).batchUpdate(any(), any(), any())
    }

    @Test
    fun `should update budget configuration with previous setup`() {
        val userId = randomUUID()
        val oldAvailableFrom = YearMonth.of(2021, Month.JANUARY)
        val newAvailableFrom = YearMonth.of(2021, Month.FEBRUARY)
        val existingBudgets = listOf(
            budget(randomUUID(), userId, "Needs", version(oldAvailableFrom, 50, listOf(randomUUID(), randomUUID()))),
            budget(randomUUID(), userId, "Wants", version(oldAvailableFrom, 30, listOf(randomUUID(), randomUUID()))),
            budget(randomUUID(), userId, "Savings", version(oldAvailableFrom, 15, listOf(randomUUID()))),
            budget(randomUUID(), userId, "Other", version(oldAvailableFrom, 5, listOf(randomUUID()))),
        )
        val needsExpenseTargetIds = listOf(randomUUID(), randomUUID())
        val giftsExpenseTargetIds = listOf(randomUUID())
        val command = UpdateBudgetConfigurationCommand(
            availableFrom = newAvailableFrom,
            budgets = listOf(
                UpdateBudgetCommand(
                    existingBudgets[0].id,
                    "Needs updated",
                    needsExpenseTargetIds,
                    Percent(45)
                ),
                DeleteBudgetCommand(existingBudgets[3].id),
                CreateBudgetCommand("Gifts", Percent(10), giftsExpenseTargetIds),
            )
        )
        val newBudgets = listOf(
            budget(existingBudgets[0].id, userId, "Needs updated", version(newAvailableFrom, 45, needsExpenseTargetIds)),
            existingBudgets[1],
            existingBudgets[2],
            budget(randomUUID(), userId, "Gifts", version(newAvailableFrom, 10, giftsExpenseTargetIds)),
        )
        whenever(budgetRepository.findAll(userId))
            .thenReturn(existingBudgets)
            .thenReturn(newBudgets)

        val budgetConfiguration = expenseService.updateBudgetConfiguration(userId, command)

        assertThat(budgetConfiguration).isEqualTo(
            BudgetConfiguration(
                userId,
                command.availableFrom,
                listOf(
                    BudgetVersion(existingBudgets[0].id, "Needs updated", needsExpenseTargetIds, Percent(45)),
                    BudgetVersion(
                        existingBudgets[1].id,
                        "Wants",
                        existingBudgets[1].versions[0].expenseTargetIds,
                        Percent(30)
                    ),
                    BudgetVersion(
                        existingBudgets[2].id,
                        "Savings",
                        existingBudgets[2].versions[0].expenseTargetIds,
                        Percent(15)
                    ),
                    BudgetVersion(newBudgets[3].id, "Gifts", giftsExpenseTargetIds, Percent(10)),
                )
            )
        )

        verify(budgetRepository).batchUpdate(userId, command.availableFrom, command.budgets)
    }

    @Test
    fun `should deny update of budget configuration with previous setup when percent sum is not 100`() {
        val userId = randomUUID()
        val oldAvailableFrom = YearMonth.of(2021, Month.JANUARY)
        val newAvailableFrom = YearMonth.of(2021, Month.FEBRUARY)
        val existingBudgets = listOf(
            budget(randomUUID(), userId, "Needs", version(oldAvailableFrom, 50, listOf(randomUUID(), randomUUID()))),
            budget(randomUUID(), userId, "Wants", version(oldAvailableFrom, 30, listOf(randomUUID(), randomUUID()))),
            budget(randomUUID(), userId, "Savings", version(oldAvailableFrom, 15, listOf(randomUUID()))),
            budget(randomUUID(), userId, "Other", version(oldAvailableFrom, 5, listOf(randomUUID()))),
        )
        val needsExpenseTargetIds = listOf(randomUUID(), randomUUID())
        val giftsExpenseTargetIds = listOf(randomUUID())
        val command = UpdateBudgetConfigurationCommand(
            availableFrom = newAvailableFrom,
            budgets = listOf(
                UpdateBudgetCommand(
                    existingBudgets[0].id,
                    "Needs updated",
                    needsExpenseTargetIds,
                    Percent(55)
                ),
                DeleteBudgetCommand(existingBudgets[3].id),
                CreateBudgetCommand("Gifts", Percent(10), giftsExpenseTargetIds),
            )
        )
        val newBudgets = listOf(
            budget(randomUUID(), userId, "Needs updated", version(newAvailableFrom, 45, needsExpenseTargetIds)),
            existingBudgets[1],
            existingBudgets[2],
            budget(randomUUID(), userId, "Gifts", version(newAvailableFrom, 10, giftsExpenseTargetIds)),
        )
        whenever(budgetRepository.findAll(userId))
            .thenReturn(existingBudgets)
            .thenReturn(newBudgets)

        assertThatThrownBy { expenseService.updateBudgetConfiguration(userId, command) }
            .isInstanceOf(BudgetException::class.java)
            .hasMessage(BUDGET_PERCENT_SUM_NOT_100)

        verify(budgetRepository, never()).batchUpdate(any(), any(), any())
    }

    private fun budget(id: UUID, userId: UUID, name: String, vararg versions: Budget.Version) =
        Budget(id, userId, name, versions.toList())

    private fun version(availableFrom: YearMonth, incomePercent: Int, expenseTargets: List<UUID> = emptyList()) =
        Budget.Version(availableFrom, Percent(incomePercent), expenseTargets)
}