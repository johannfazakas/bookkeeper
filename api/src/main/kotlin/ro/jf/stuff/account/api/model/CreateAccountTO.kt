package ro.jf.stuff.account.api.model

import ro.jf.stuff.account.persistence.entity.Account

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
