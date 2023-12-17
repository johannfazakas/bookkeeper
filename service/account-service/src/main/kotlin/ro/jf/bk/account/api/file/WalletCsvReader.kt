package ro.jf.bk.account.api.file

import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReader
import com.opencsv.CSVReaderBuilder
import mu.KotlinLogging.logger
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import ro.jf.bk.account.api.transfer.TransactionImportTO
import java.math.BigDecimal

val log = logger { }

@Component
class ImportCsvReader {
    companion object {
        private const val SEPARATOR = ';'

        private const val ACCOUNT_NAME_HEADER = "account"
        private const val AMOUNT_HEADER = "amount"
        private const val CURRENCY_HEADER = "currency"
        private const val PAYEE_HEADER = "payee"
        private const val NOTE_HEADER = "note"
        private const val LABELS_HEADER = "labels"
    }

    fun read(file: MultipartFile): List<TransactionImportTO> {
        val reader = csvReader(file)
        reader.use {
            val headers = it.first()
            val rowReader = createRowReader(headers)
            return it.map(rowReader)
        }
    }

    fun createRowReader(headers: Array<String>): (Array<String>) -> TransactionImportTO {
        val accountNameIndex = headers.indexOf(ACCOUNT_NAME_HEADER)
        val amountIndex = headers.indexOf(AMOUNT_HEADER)
        val currencyIndex = headers.indexOf(CURRENCY_HEADER)
        val payeeIndex = headers.indexOf(PAYEE_HEADER)
        val noteIndex = headers.indexOf(NOTE_HEADER)
        val labelsIndex = headers.indexOf(LABELS_HEADER)
        return { row ->
            TransactionImportTO(
                accountName = row[accountNameIndex],
                amount = BigDecimal(row[amountIndex]),
                currency = row[currencyIndex],
                // TODO(Johann) what happens on multiple labels?
                category = row[labelsIndex].split(',')[0],
                metadata = mapOf(PAYEE_HEADER to row[payeeIndex], NOTE_HEADER to row[noteIndex]),
            )
        }
    }

    private fun csvReader(file: MultipartFile): CSVReader =
        CSVReaderBuilder(file.inputStream.bufferedReader())
            .withCSVParser(
                CSVParserBuilder()
                    .withSeparator(SEPARATOR)
                    .build()
            )
            .build()
}
