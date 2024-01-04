package ro.jf.bk.account.domain.command

import java.math.BigDecimal
import java.time.Instant

data class ImportTransactionCommand(
    val timestamp: Instant,
    val fromAccountReference: String,
    val toAccountReference: String,
    val amount: BigDecimal,
    val currency: String,
    val description: String?,
    val metadata: Map<String, Any?>,
)
