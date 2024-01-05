package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.command.CreateBudgetCommand
import ro.jf.bk.account.domain.command.DeleteBudgetCommand
import ro.jf.bk.account.domain.command.UpdateBudgetCommand
import ro.jf.bk.account.domain.command.UpdateBudgetConfigurationCommand
import ro.jf.bk.account.domain.error.BUDGET_PERCENT_SUM_NOT_100
import ro.jf.bk.account.domain.error.BudgetException
import ro.jf.bk.account.domain.error.NO_VERSION_FOR_YEAR_MONTH
import ro.jf.bk.account.domain.model.Budget
import ro.jf.bk.account.domain.model.BudgetConfiguration
import java.time.YearMonth
import java.util.*

@Service
class ExpenseService(
    private val budgetRepository: BudgetRepository
) {
    fun updateBudgetConfiguration(userId: UUID, command: UpdateBudgetConfigurationCommand): BudgetConfiguration {
        validateCommand(userId, command)
        budgetRepository.batchUpdate(userId, command.availableFrom, command.budgets)
        return budgetRepository.findAll(userId)
            .let { BudgetConfiguration.fromBudgets(userId, command.availableFrom, it) }
    }

    private fun validateCommand(userId: UUID, command: UpdateBudgetConfigurationCommand) {
        val existingBudgets = budgetRepository.findAll(userId)
        validateCommandPercentSum(command, existingBudgets)
    }

    private fun validateCommandPercentSum(command: UpdateBudgetConfigurationCommand, existingBudgets: List<Budget>) {
        val modifiedBudgetIds = command.budgets
            .mapNotNull {
                when (it) {
                    is CreateBudgetCommand -> null
                    is UpdateBudgetCommand -> it.id
                    is DeleteBudgetCommand -> it.id
                }
            }

        val previousBudgetPercentSum = existingBudgets
            .filter { it.id !in modifiedBudgetIds }
            .sumOf { getBudgetVersion(it, command.availableFrom).incomePercent.value }

        val newBudgetPercentSum = command.budgets
            .sumOf {
                when (it) {
                    is CreateBudgetCommand -> it.incomePercent.value
                    is UpdateBudgetCommand -> it.incomePercent.value
                    is DeleteBudgetCommand -> 0
                }
            }

        if (previousBudgetPercentSum + newBudgetPercentSum != 100) throw BudgetException(BUDGET_PERCENT_SUM_NOT_100)
    }

    private fun getBudgetVersion(budget: Budget, yearMonth: YearMonth): Budget.Version =
        budget.getVersion(yearMonth) ?: throw BudgetException(NO_VERSION_FOR_YEAR_MONTH)
}
