package ro.jf.bk.account.domain.command

import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class CreateTransactionCommand(
    val timestamp: Instant,
    val from: UUID,
    val to: UUID,
    val amount: BigDecimal,
    val description: String?
)
