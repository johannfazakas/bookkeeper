package ro.jf.bk.account.api.transfer

import ro.jf.bk.account.domain.model.CreateAccountCommand

data class CreateAccountTO(
    val name: String,
    val currency: String
) {
    fun toCommand() = CreateAccountCommand(
        name = name,
        currency = currency
    )
}
