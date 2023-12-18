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
}
