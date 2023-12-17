package ro.jf.bk.account.api.web

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.jf.bk.account.api.file.ImportCsvReader
import ro.jf.bk.account.api.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.api.transfer.ImportResultTO
import ro.jf.bk.account.domain.service.ImportService
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/import/")
class ImportController(
    private val importCsvReader: ImportCsvReader,
    private val importService: ImportService
) {
    @PutMapping("/wallet/csv")
    @ResponseStatus(HttpStatus.OK)
    fun importTransactions(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestParam("file") file: MultipartFile
    ): ImportResultTO {
        log.info { "Importing transactions for user $userId from CSV file." }
        importService.import(userId, importCsvReader.read(file))
        return ImportResultTO(0, 0)
    }
}
