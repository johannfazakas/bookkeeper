package ro.jf.bk.account.domain.command

import ro.jf.bk.account.domain.model.AccountType

data class CreateAccountCommand(
    val name: String,
    val type: AccountType,
    val currency: String,
    val externalReference: String?
)
