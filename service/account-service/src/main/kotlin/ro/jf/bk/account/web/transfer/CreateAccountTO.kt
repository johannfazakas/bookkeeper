package ro.jf.bk.account.web.transfer

import com.fasterxml.jackson.annotation.JsonProperty
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

data class CreateAccountTO(
    @JsonProperty("user_id")
    val userId: UUID,
    val name: String,
    val currency: String
) {
    fun toCommand() = CreateAccountCommand(
        userId = userId,
        name = name,
        currency = currency
    )
}
