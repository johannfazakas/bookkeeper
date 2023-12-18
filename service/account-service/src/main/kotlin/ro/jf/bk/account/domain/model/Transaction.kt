package ro.jf.bk.account.domain.model

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class Transaction(
    val id: UUID,
    val userId: UUID,
    val timestamp: Instant,
    val from: UUID,
    val to: UUID,
    val amount: BigDecimal,
    val description: String?
)
