package ro.jf.bk.account.domain.model

import java.time.YearMonth
import java.util.*

data class Budget(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val versions: List<Version>
) {
    fun getVersion(yearMonth: YearMonth): Version? =
        versions
            .sortedByDescending { it.availableFrom }
            .dropWhile { it.availableFrom > yearMonth }
            .firstOrNull()

    data class Version(
        val availableFrom: YearMonth,
        val incomePercent: Percent,
        val expenseTargetIds: List<UUID>,
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
    val budgets: List<BudgetVersion>
) {
    companion object {
        fun fromBudgets(userId: UUID, availableFrom: YearMonth, budgets: List<Budget>): BudgetConfiguration =
            BudgetConfiguration(
                userId = userId,
                availableFrom = availableFrom,
                budgets = budgets.mapNotNull { budget ->
                    budget.getVersion(availableFrom)?.let {
                        BudgetVersion(
                            budgetId = budget.id,
                            name = budget.name,
                            expenseTargetIds = it.expenseTargetIds,
                            incomePercent = it.incomePercent,
                        )
                    }
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
