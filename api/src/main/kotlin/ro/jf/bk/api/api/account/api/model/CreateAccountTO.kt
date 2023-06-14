package ro.jf.bk.api.api.account.api.model

import ro.jf.bk.api.api.account.persistence.entity.Account

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
