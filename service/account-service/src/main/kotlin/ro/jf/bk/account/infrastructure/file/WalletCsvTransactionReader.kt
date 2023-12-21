package ro.jf.bk.account.infrastructure.file

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import mu.KotlinLogging.logger
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.error.ImportException
import ro.jf.bk.account.domain.model.ImportTransactionCommand
import ro.jf.bk.account.domain.service.ImportTransactionReader
import java.io.InputStream
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ofPattern

val log = logger { }

@Component
class WalletCsvTransactionReader : ImportTransactionReader {
    companion object {
        private const val SEPARATOR = ';'
    }

    override val exporter: String = "wallet-csv"

    override fun read(inputStream: InputStream): List<ImportTransactionCommand> {
        csvReader(inputStream).use { csvReader ->
            val rows = csvReader.toList()
            val headers = Headers(rows.first())
            val entryReader = EntryReader(headers)
            val entries = rows.drop(1).map(entryReader::read)
            return entries
                .groupBy(Entry::transactionKey)
                .map { (_, entries) -> entries.toImportTransactionCommand() }
        }
    }

    private fun csvReader(inputStream: InputStream): CSVReader =
        CSVReaderBuilder(inputStream.bufferedReader())
            .withCSVParser(
                CSVParserBuilder()
                    .withSeparator(SEPARATOR)
                    .build()
            )
            .build()

    private class Headers(val values: Array<String>) {
        companion object {
            const val ACCOUNT_NAME_HEADER = "account"
            const val AMOUNT_HEADER = "amount"
            const val CURRENCY_HEADER = "currency"
            const val PAYEE_HEADER = "payee"
            const val NOTE_HEADER = "note"
            const val TIMESTAMP_HEADER = "date"
            const val LABELS_HEADER = "labels"
            const val TRANSFER_FLAG_HEADER = "transfer"
        }
    }

    private data class Entry(
        val accountName: String,
        val currency: String,
        val amount: BigDecimal,
        val note: String?,
        val payee: String?,
        val instant: Instant,
        val isTransfer: Boolean,
        val labels: List<String>,
    ) {
        fun transactionKey(): String = "$instant:${amount.abs()}$currency"
    }

    private class EntryReader(private val headers: Headers) {
        private val accountNameIndex = getHeaderIndex(Headers.ACCOUNT_NAME_HEADER)
        private val currencyIndex = getHeaderIndex(Headers.CURRENCY_HEADER)
        private val amountIndex = getHeaderIndex(Headers.AMOUNT_HEADER)
        private val payeeIndex = getHeaderIndex(Headers.PAYEE_HEADER)
        private val noteIndex = getHeaderIndex(Headers.NOTE_HEADER)
        private val timestampIndex = getHeaderIndex(Headers.TIMESTAMP_HEADER)
        private val labelsIndex = getHeaderIndex(Headers.LABELS_HEADER)

        private val transferFlagIndex = getHeaderIndex(Headers.TRANSFER_FLAG_HEADER)

        fun read(row: Array<String>): Entry = try {
            Entry(
                accountName = row[accountNameIndex]!!,
                currency = row[currencyIndex],
                amount = BigDecimal(row[amountIndex]),
                note = row[noteIndex],
                payee = row[payeeIndex],
                instant = row[timestampIndex].toInstant(),
                isTransfer = row[transferFlagIndex].toBoolean(),
                labels = row[labelsIndex].split(",").map { it.trim() }
            )
        } catch (e: Exception) {
            throw ImportException("Error reading row $row", e)
        }

        private fun getHeaderIndex(header: String): Int {
            val index = headers.values.indexOf(header)
            if (index < 0) throw ImportException("Header $header not found in csv file.")
            return index
        }

        private fun String.toInstant(): Instant =
            LocalDateTime.parse(this, ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(ZoneId.of("UTC"))
                .toInstant()
    }

    private fun List<Entry>.toImportTransactionCommand(): ImportTransactionCommand {
        if (map { it.transactionKey() }.distinct().size > 1) throw ImportException("Transaction entries are not matching: $this.")
        return when {
            size == 1 && !first().isTransfer && first().amount > BigDecimal.ZERO -> IncomeTransaction(first())
            size == 1 && !first().isTransfer && first().amount < BigDecimal.ZERO -> ExpenseTransaction(first())
            size == 2 && all { it.isTransfer } && first().amount != last().amount -> TransferTransaction(first(), last())
            else -> throw ImportException("Transaction entries are not invalid: $this.")
        }.toImportTransactionCommand()
    }

    private fun interface WalletTransaction {
        fun toImportTransactionCommand(): ImportTransactionCommand
    }

    private class TransferTransaction(first: Entry, second: Entry) : WalletTransaction {
        private val sender = listOf(first, second).minBy { it.amount }
        private val receiver = listOf(first, second).maxBy { it.amount }

        override fun toImportTransactionCommand() = ImportTransactionCommand(
            timestamp = sender.instant,
            fromAccountReference = sender.accountName,
            toAccountReference = receiver.accountName,
            amount = receiver.amount,
            currency = sender.currency,
            description = sender.note,
            metadata = mapOf(
                "payee" to sender.payee,
                "labels" to sender.labels
            )
        )
    }

    private class IncomeTransaction(val entry: Entry) : WalletTransaction {
        override fun toImportTransactionCommand() = ImportTransactionCommand(
            timestamp = entry.instant,
            fromAccountReference = entry.labels.first(),
            toAccountReference = entry.accountName,
            amount = entry.amount,
            currency = entry.currency,
            description = entry.note,
            metadata = mapOf(
                "payee" to entry.payee,
                "labels" to entry.labels
            )
        )
    }

    private class ExpenseTransaction(val entry: Entry) : WalletTransaction {
        override fun toImportTransactionCommand() = ImportTransactionCommand(
            timestamp = entry.instant,
            fromAccountReference = entry.accountName,
            toAccountReference = entry.labels.first(),
            amount = entry.amount.abs(),
            currency = entry.currency,
            description = entry.note,
            metadata = mapOf(
                "payee" to entry.payee,
                "labels" to entry.labels
            )
        )
    }
}
