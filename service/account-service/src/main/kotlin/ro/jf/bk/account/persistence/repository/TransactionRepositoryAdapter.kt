package ro.jf.bk.account.persistence.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.model.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import ro.jf.bk.account.domain.service.TransactionRepository
import ro.jf.bk.account.persistence.entity.TransactionEntity
import java.util.*

interface TransactionEntityRepository : CrudRepository<TransactionEntity, UUID> {
    @Query(
        """
        SELECT * 
        FROM transaction t 
        WHERE t.user_id = :userId 
        AND (t.from_account_id = :accountId OR t.to_account_id = :accountId) 
        ORDER BY t.timestamp DESC
        """,
        nativeQuery = true
    )
    fun findByUserIdAndAccount(userId: UUID, accountId: UUID): List<TransactionEntity>
}

@Component
@Transactional
class TransactionRepositoryAdapter(
    private val transactionEntityRepository: TransactionEntityRepository
) : TransactionRepository {
    override fun save(userId: UUID, command: CreateTransactionCommand): Transaction =
        transactionEntityRepository.save(
            TransactionEntity(
                userId = userId,
                timestamp = command.timestamp,
                from = command.from,
                to = command.to,
                amount = command.amount,
                description = command.description
            )
        ).toModel()
}
