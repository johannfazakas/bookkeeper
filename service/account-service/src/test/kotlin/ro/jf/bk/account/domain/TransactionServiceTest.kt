package ro.jf.bk.account.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import ro.jf.bk.account.domain.model.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import ro.jf.bk.account.domain.service.ImportTransactionReaderRegistry
import ro.jf.bk.account.domain.service.TransactionRepository
import ro.jf.bk.account.domain.service.TransactionService
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID.randomUUID

class TransactionServiceTest {
    private val transactionRepository = mock<TransactionRepository>()
    private val importTransactionReaderRegistry = mock<ImportTransactionReaderRegistry>()
    private val transactionService = TransactionService(transactionRepository, importTransactionReaderRegistry)

    @Test
    fun shouldCreateTransaction() {
        val userId = randomUUID()
        val fromAccountId = randomUUID()
        val toAccountId = randomUUID()
        val transactionId = randomUUID()
        val createCommand =
            CreateTransactionCommand(Instant.now(), fromAccountId, toAccountId, BigDecimal(12.5), "transaction-name")
        whenever(transactionRepository.save(eq(userId), any<CreateTransactionCommand>()))
            .thenAnswer {
                val command = it.arguments[1] as CreateTransactionCommand
                command.run { Transaction(transactionId, userId, timestamp, from, to, amount, description) }
            }

        val transaction = transactionService.create(userId, createCommand)

        assertThat(transaction).isNotNull()
        assertThat(transaction.id).isEqualTo(transactionId)
        assertThat(transaction.userId).isEqualTo(userId)
        assertThat(transaction.timestamp).isEqualTo(createCommand.timestamp)
        assertThat(transaction.from).isEqualTo(fromAccountId)
        assertThat(transaction.to).isEqualTo(toAccountId)
        assertThat(transaction.amount).isEqualTo(createCommand.amount)
    }

    @Test
    fun shouldRetrieveTransaction() {
        val userId = randomUUID()
        val transactionId = randomUUID()
        val transaction = Transaction(
            transactionId,
            userId,
            Instant.now(),
            randomUUID(),
            randomUUID(),
            BigDecimal(12.5),
            "transaction-name"
        )
        whenever(transactionRepository.getById(userId, transactionId)).thenReturn(transaction)

        val retrievedTransaction = transactionService.getById(userId, transactionId)

        assertThat(retrievedTransaction).isNotNull()
        assertThat(retrievedTransaction?.id).isEqualTo(transactionId)
        assertThat(retrievedTransaction?.userId).isEqualTo(userId)
        assertThat(retrievedTransaction?.timestamp).isEqualTo(transaction.timestamp)
        assertThat(retrievedTransaction?.from).isEqualTo(transaction.from)
        assertThat(retrievedTransaction?.to).isEqualTo(transaction.to)
        assertThat(retrievedTransaction?.amount).isEqualTo(transaction.amount)
    }

    @Test
    fun shouldRetrieveTransactionsByAccountId() {
        val userId = randomUUID()
        val accountId = randomUUID()
        val transaction1 = Transaction(
            randomUUID(),
            userId,
            Instant.now(),
            accountId,
            randomUUID(),
            BigDecimal(12.5),
            "transaction-name"
        )
        val transaction2 = Transaction(
            randomUUID(),
            userId,
            Instant.now(),
            accountId,
            randomUUID(),
            BigDecimal(12.5),
            "transaction-name"
        )
        whenever(transactionRepository.listByAccountId(userId, accountId)).thenReturn(
            listOf(
                transaction1,
                transaction2
            )
        )

        val transactions = transactionService.listByAccountId(userId, accountId)

        assertThat(transactions).hasSize(2)
        assertThat(transactions[0].id).isEqualTo(transaction1.id)
        assertThat(transactions[0].userId).isEqualTo(userId)
        assertThat(transactions[0].timestamp).isEqualTo(transaction1.timestamp)
        assertThat(transactions[0].from).isEqualTo(transaction1.from)
        assertThat(transactions[0].to).isEqualTo(transaction1.to)
        assertThat(transactions[0].amount).isEqualTo(transaction1.amount)
        assertThat(transactions[1].id).isEqualTo(transaction2.id)
    }

    @Test
    fun shouldDeleteTransaction() {
        val userId = randomUUID()
        val transactionId = randomUUID()

        transactionService.deleteById(userId, transactionId)

        verify(transactionRepository).deleteById(userId, transactionId)
    }
}
