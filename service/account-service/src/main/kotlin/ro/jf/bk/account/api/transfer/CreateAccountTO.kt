package ro.jf.bk.account.api.transfer

import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.model.CreateAccountCommand

data class CreateAccountTO(
    val name: String,
    val currency: String,
    val externalReference: String?
) {
    fun toCommand(accountType: AccountType) = CreateAccountCommand(
        name = name,
        type = accountType,
        currency = currency,
        externalReference = externalReference
    )
}
