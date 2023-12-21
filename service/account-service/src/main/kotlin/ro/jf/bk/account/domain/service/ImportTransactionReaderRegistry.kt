package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.model.ImportTransactionCommand
import java.io.InputStream

interface ImportTransactionReader {
    val exporter: String
    fun read(inputStream: InputStream): List<ImportTransactionCommand>
}

@Component
class ImportTransactionReaderRegistry(
    private val readers: List<ImportTransactionReader>
) {
    private val readersByExporter: Map<String, ImportTransactionReader> = readers.associateBy { it.exporter }

    operator fun get(exporter: String): ImportTransactionReader? = readersByExporter[exporter]
}
