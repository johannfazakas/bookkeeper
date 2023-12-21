package ro.jf.bk.account.api.web

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.jf.bk.account.api.transfer.*
import ro.jf.bk.account.api.web.error.TransactionNotFoundException
import ro.jf.bk.account.api.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.domain.service.TransactionService
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/transactions")
class
TransactionController(
    private val transactionService: TransactionService
) {
    @PostMapping
    fun createTransaction(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestBody request: CreateTransactionTO
    ): TransactionTO {
        log.info { "Create transaction >> user: $userId, request: $request." }
        return transactionService.create(userId, request.toCommand()).toTO()
    }

    @GetMapping("/{transactionId}")
    fun getTransaction(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("transactionId") transactionId: UUID
    ): TransactionTO {
        log.debug { "Get transaction >> user: $userId, transactionId: $transactionId." }
        return transactionService.getById(userId, transactionId)?.toTO()
            ?: throw TransactionNotFoundException(userId, transactionId)
    }

    @GetMapping
    fun listTransactions(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestParam("accountId", required = true) accountId: UUID
    ): ListTO<TransactionTO> {
        log.debug { "List transactions >> user: $userId, accountId: $accountId." }
        return transactionService.listByAccountId(userId, accountId).map { it.toTO() }.toListTO()
    }

    @DeleteMapping("/{transactionId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteTransaction(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("transactionId") transactionId: UUID
    ) {
        log.info { "Delete transaction >> user: $userId, transactionId: $transactionId." }
        transactionService.deleteById(userId, transactionId)
    }

    @PutMapping("/import/{exporter}")
    @ResponseStatus(HttpStatus.OK)
    fun importTransactions(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("exporter") exporter: String,
        @RequestParam("file") file: MultipartFile
    ): ImportResultTO {
        log.info { "Importing transactions for user $userId from CSV file." }
        transactionService.import(userId, exporter, file.inputStream)
        return ImportResultTO(0, 0)
    }
}
