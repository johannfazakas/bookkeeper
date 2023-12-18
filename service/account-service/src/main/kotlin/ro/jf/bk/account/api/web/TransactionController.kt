package ro.jf.bk.account.api.web

import mu.KotlinLogging.logger
import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.api.transfer.CreateTransactionTO
import ro.jf.bk.account.api.transfer.TransactionTO
import ro.jf.bk.account.api.transfer.TransactionTO.Companion.toTO
import ro.jf.bk.account.api.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.domain.service.TransactionService
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/transactions")
class TransactionController(
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
}
