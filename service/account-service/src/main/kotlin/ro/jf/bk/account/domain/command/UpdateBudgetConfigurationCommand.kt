package ro.jf.bk.account.domain.command

import ro.jf.bk.account.domain.model.Budget
import java.time.YearMonth
import java.util.*

data class UpdateBudgetConfigurationCommand(
    val availableFrom: YearMonth,
    val budgets: List<BudgetCommand>,
)

sealed class BudgetCommand

data class CreateBudgetCommand(
    val name: String,
    val incomePercent: Budget.Percent,
    val expenseTargetIds: List<UUID>,
) : BudgetCommand()

data class UpdateBudgetCommand(
    val id: UUID,
    val name: String,
    val expenseTargetIds: List<UUID>,
    val incomePercent: Budget.Percent,
) : BudgetCommand()

data class DeleteBudgetCommand(
    val id: UUID,
) : BudgetCommand()
