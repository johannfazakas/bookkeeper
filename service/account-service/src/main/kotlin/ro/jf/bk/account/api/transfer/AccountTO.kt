package ro.jf.bk.account.api.transfer

import com.fasterxml.jackson.annotation.JsonProperty
import ro.jf.bk.account.domain.model.Account
import java.util.*

data class AccountTO(
    val id: UUID,
    @JsonProperty("user_id")
    val userId: UUID,
    val name: String,
    val currency: String
)

fun Account.toTO(): AccountTO = AccountTO(
    id = this.id,
    userId = this.userId,
    name = this.name,
    currency = this.currency
)
