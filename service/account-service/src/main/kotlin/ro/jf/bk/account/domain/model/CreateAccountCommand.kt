package ro.jf.bk.account.domain.model

data class CreateAccountCommand(
    val name: String,
    val type: AccountType,
    val currency: String,
    val externalReference: String?
)
