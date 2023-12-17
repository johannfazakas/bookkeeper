package ro.jf.bk.account.web

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.jf.bk.account.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.web.transfer.ImportResultTO
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/transactions/import/")
class TransactionsImportController {
    @PutMapping("/csv")
    @ResponseStatus(HttpStatus.OK)
    fun importTransactions(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestParam("file") file: MultipartFile
    ): ImportResultTO {
        log.info { "Importing transactions for user $userId from CSV file." }
        // TODO(Johann) Implement
        return ImportResultTO(0, 0)
    }
}
