package ro.jf.bk.account.infrastructure.persistence.repository

import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.command.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import ro.jf.bk.account.domain.service.TransactionRepository
import ro.jf.bk.account.infrastructure.persistence.entity.TransactionEntity
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

    fun findByUserId(userId: UUID): List<TransactionEntity>

    fun findByUserIdAndId(userId: UUID, id: UUID): TransactionEntity?

    fun deleteByUserIdAndId(userId: UUID, id: UUID)
}

@Component
@Transactional
class TransactionRepositoryAdapter(
    private val transactionEntityRepository: TransactionEntityRepository
) : TransactionRepository {
    override fun save(userId: UUID, command: CreateTransactionCommand): Transaction =
        transactionEntityRepository.save(TransactionEntity.fromCommand(userId, command)).toModel()

    override fun saveAll(userId: UUID, commands: List<CreateTransactionCommand>): List<Transaction> =
        transactionEntityRepository
            .saveAll(commands.map { TransactionEntity.fromCommand(userId, it) })
            .map { it.toModel() }

    override fun getById(userId: UUID, transactionId: UUID): Transaction? =
        transactionEntityRepository.findByUserIdAndId(userId, transactionId)?.toModel()

    override fun listByAccountId(userId: UUID, accountId: UUID): List<Transaction> =
        transactionEntityRepository.findByUserIdAndAccount(userId, accountId).map { it.toModel() }

    override fun deleteById(userId: UUID, transactionId: UUID) {
        transactionEntityRepository.deleteByUserIdAndId(userId, transactionId)
    }
}
