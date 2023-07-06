package ro.jf.bk.account.web.transfer

import ro.jf.bk.account.persistence.model.Account

data class CreateAccountTO(
    var name: String,
    var currency: String
) {
    fun toDomain() = Account(
        id = null,
        name = name,
        currency = currency
    )
}
