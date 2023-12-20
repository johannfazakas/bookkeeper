package ro.jf.bk.account.domain.model

enum class AccountType(val value: String) {
    PRIVATE("personal"),
    INCOME_SOURCE("income_source"),
    EXPENSE_TARGET("expense_target")
    ;

    companion object {
        fun fromValue(value: String): AccountType {
            return values().first { it.value == value }
        }
    }
}
