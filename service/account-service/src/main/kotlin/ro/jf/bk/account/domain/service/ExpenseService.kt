package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.command.UpdateBudgetConfigurationCommand
import ro.jf.bk.account.domain.model.BudgetConfiguration
import java.util.*

@Service
class ExpenseService(
    private val budgetRepository: BudgetRepository
) {
    fun updateBudgetConfiguration(userId: UUID, command: UpdateBudgetConfigurationCommand): BudgetConfiguration {
        budgetRepository.batchUpdate(userId, command.availableFrom, command.budgets)
        val budgets = budgetRepository.findAll(userId)
        return BudgetConfiguration.fromBudgets(userId, command.availableFrom, budgets)
    }
}
