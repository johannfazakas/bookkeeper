package ro.jf.bk.account.domain.model

import java.time.YearMonth
import java.util.*

data class Budget(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val versions: List<Version>
) {
    data class Version(
        val availableFrom: YearMonth,
        val expenseTargetIds: List<UUID>,
        val incomePercent: Percent,
    )

    @JvmInline
    value class Percent(val value: Int) {
        init {
            require(value in 0..100) { "Percent value must be between 0 and 100" }
        }
    }
}

data class BudgetConfiguration(
    val userId: UUID,
    val availableFrom: YearMonth,
    val budgets: List<BudgetConfiguration.BudgetVersion>
) {
    companion object {
        fun fromBudgets(userId: UUID, availableFrom: YearMonth, budgets: List<Budget>): BudgetConfiguration =
            BudgetConfiguration(
                userId = userId,
                availableFrom = availableFrom,
                budgets = budgets.map { budget ->
                    val version = budget.versions
                        .sortedBy { it.availableFrom }
                        .dropWhile { it.availableFrom < availableFrom }
                        .first()
                    BudgetVersion(
                        budgetId = budget.id,
                        name = budget.name,
                        expenseTargetIds = version.expenseTargetIds,
                        incomePercent = version.incomePercent,
                    )
                }
            )
    }

    data class BudgetVersion(
        val budgetId: UUID,
        val name: String,
        val expenseTargetIds: List<UUID>,
        val incomePercent: Budget.Percent,
    )
}
