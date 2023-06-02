package ro.jf.stuff.account.api.transfer

import java.util.*

data class AccountTO(
        val id: UUID,
        val name: String,
        val currency: String
)
