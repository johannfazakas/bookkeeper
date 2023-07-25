package ro.jf.bk.account.domain.model

data class CreateAccountCommand(
    val name: String,
    val currency: String
)
