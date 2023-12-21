package ro.jf.bk.account.infrastructure.persistence.entity

import jakarta.persistence.*
import ro.jf.bk.account.domain.model.Transaction
import java.math.BigDecimal
import java.time.Instant
import java.util.*

@Entity
@Table(name = "transaction")
data class TransactionEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    @Column(name = "user_id")
    var userId: UUID,
    var timestamp: Instant,
    @Column(name = "from_account_id")
    var from: UUID,
    @Column(name = "to_account_id")
    var to: UUID,
    var amount: BigDecimal,
    var description: String?
) {
    fun toModel(): Transaction {
        if (id == null) throw IllegalStateException("Transaction Entity id is null.")
        return Transaction(
            id = this.id!!,
            userId = this.userId,
            timestamp = this.timestamp,
            from = this.from,
            to = this.to,
            amount = this.amount,
            description = this.description
        )
    }
}
