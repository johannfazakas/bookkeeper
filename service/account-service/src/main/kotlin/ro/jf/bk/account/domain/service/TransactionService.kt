package ro.jf.bk.account.domain.service

import mu.KotlinLogging.logger
import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.error.ImportException
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.command.CreateTransactionCommand
import ro.jf.bk.account.domain.model.Transaction
import java.io.InputStream
import java.util.*

private val log = logger { }

@Service
class TransactionService(
    private val transactionRepository: TransactionRepository,
    private val accountService: AccountService,
    private val importTransactionReaderRegistry: ImportTransactionReaderRegistry,
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

    fun import(userId: UUID, exporter: String, inputStream: InputStream) {
        val importTransactionCommands = importTransactionReaderRegistry[exporter]
            ?.read(inputStream)
            ?: throw ImportException("No reader found for exporter: $exporter.")
        log.info { "Importing transactions >> user: $userId, exporter: $exporter, transactions: $importTransactionCommands." }

        val accountIdResolver = getAccountIdResolverByReference(userId)
        val createTransactionCommands = importTransactionCommands
            .map {
                CreateTransactionCommand(
                    timestamp = it.timestamp,
                    from = accountIdResolver(it.fromAccountReference),
                    to = accountIdResolver(it.toAccountReference),
                    amount = it.amount,
                    description = it.description
                )
            }
        transactionRepository.saveAll(userId, createTransactionCommands)
    }

    private fun getAccountIdResolverByReference(userId: UUID): (String) -> UUID {
        val accountsByReference = accountService
            .findAll(userId)
            .associateBy(Account::externalReference, Account::id)
        return { accountsByReference[it] ?: throw ImportException("No account found for reference: $it.") }
    }
}
