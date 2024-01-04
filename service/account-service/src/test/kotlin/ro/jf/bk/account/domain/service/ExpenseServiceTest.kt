package ro.jf.bk.account.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ro.jf.bk.account.domain.command.CreateBudgetCommand
import ro.jf.bk.account.domain.command.UpdateBudgetConfigurationCommand
import ro.jf.bk.account.domain.model.Budget
import ro.jf.bk.account.domain.model.Budget.Percent
import ro.jf.bk.account.domain.model.BudgetConfiguration
import java.time.Month
import java.time.YearMonth
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
                CreateBudgetCommand("Needs", listOf(targetIds[0], targetIds[1]), Percent(50)),
                CreateBudgetCommand("Wants", listOf(targetIds[2], targetIds[3]), Percent(30)),
                CreateBudgetCommand("Savings", listOf(targetIds[4]), Percent(20)),
            )
        )
        val budgetId1 = randomUUID()
        val budgetId2 = randomUUID()
        val budgetId3 = randomUUID()
        val budget1Version = Budget.Version(availableFrom, listOf(targetIds[0], targetIds[1]), Percent(50))
        val budget2Version = Budget.Version(availableFrom, listOf(targetIds[2], targetIds[3]), Percent(30))
        val budget3Version = Budget.Version(availableFrom, listOf(targetIds[4]), Percent(20))
        whenever(budgetRepository.findAll(userId))
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
                userId = userId,
                availableFrom = command.availableFrom,
                budgets = listOf(
                    BudgetConfiguration.BudgetVersion(
                        budgetId = budgetId1,
                        name = "Needs",
                        expenseTargetIds = listOf(targetIds[0], targetIds[1]),
                        incomePercent = Percent(50),
                    ),
                    BudgetConfiguration.BudgetVersion(
                        budgetId = budgetId2,
                        name = "Wants",
                        expenseTargetIds = listOf(targetIds[2], targetIds[3]),
                        incomePercent = Percent(30),
                    ),
                    BudgetConfiguration.BudgetVersion(
                        budgetId = budgetId3,
                        name = "Savings",
                        expenseTargetIds = listOf(targetIds[4]),
                        incomePercent = Percent(20),
                    ),
                )
            )
        )

        verify(budgetRepository).batchUpdate(userId, command.availableFrom, command.budgets)
    }
}