package ro.jf.bk.account.domain.service

import ro.jf.bk.account.domain.model.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import java.util.*

interface TransactionRepository {
    fun save(userId: UUID, command: CreateTransactionCommand): Transaction
    fun getById(userId: UUID, transactionId: UUID): Transaction?
    fun listByAccountId(userId: UUID, accountId: UUID): List<Transaction>
    fun deleteById(userId: UUID, transactionId: UUID)
}
