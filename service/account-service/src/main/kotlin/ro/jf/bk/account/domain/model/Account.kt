package ro.jf.bk.account.domain.model

import java.util.*

data class Account(
    val id: UUID,
    val userId: UUID,
    val name: String,
    val currency: String
)
