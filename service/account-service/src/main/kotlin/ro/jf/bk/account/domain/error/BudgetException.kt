package ro.jf.bk.account.domain.error

const val BUDGET_PERCENT_SUM_NOT_100 = "Budgets' percentage sum is not 100"
const val NO_VERSION_FOR_YEAR_MONTH = "No version for year month"

class BudgetException(message: String) : RuntimeException(message)
