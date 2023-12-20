package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.model.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import java.util.*

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository
) {
    fun create(userId: UUID, command: CreateTransactionCommand): Transaction =
        transactionRepository.save(userId, command)

    fun getById(userId: UUID, transactionId: UUID): Transaction? =
        transactionRepository.getById(userId, transactionId)

    fun listByAccountId(userId: UUID, accountId: UUID): List<Transaction> =
        transactionRepository.listByAccountId(userId, accountId)

    fun deleteById(userId: UUID, transactionId: UUID) {
        transactionRepository.deleteById(userId, transactionId)
    }
}
