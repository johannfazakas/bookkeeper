package ro.jf.bk.account.domain.model

import java.util.*

data class CreateAccountCommand(
    val userId: UUID,
    val name: String,
    val currency: String
)
