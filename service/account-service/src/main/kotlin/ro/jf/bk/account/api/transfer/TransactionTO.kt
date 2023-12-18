package ro.jf.bk.account.api.transfer

import ro.jf.bk.account.domain.model.Transaction
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class TransactionTO(
    val id: UUID,
    val userId: UUID,
    val timestamp: Instant,
    val from: UUID,
    val to: UUID,
    val amount: BigDecimal,
    val description: String?
) {
    companion object {
        fun Transaction.toTO() = TransactionTO(
            id = this.id,
            userId = this.userId,
            timestamp = this.timestamp,
            from = this.from,
            to = this.to,
            amount = this.amount,
            description = this.description
        )
    }
}