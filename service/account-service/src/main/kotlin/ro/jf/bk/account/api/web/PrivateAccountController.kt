package ro.jf.bk.account.api.web

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.api.transfer.*
import ro.jf.bk.account.api.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.service.AccountService
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/private-accounts")
class PrivateAccountController(
    private val accountService: AccountService
) {
    @GetMapping("/{accountId}")
    fun getAccount(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ): AccountTO {
        log.debug { "Get personal account >> user: $userId, accountId: $accountId." }
        return accountService.find(userId, AccountType.PRIVATE, accountId)?.toTO()
            ?: throw RuntimeException("Account not found")
    }

    @GetMapping
    fun listAccounts(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID
    ): ListTO<AccountTO> {
        return accountService.list(userId, AccountType.PRIVATE).map { it.toTO() }.toListTO()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestBody request: CreateAccountTO
    ): AccountTO {
        log.info { "Create personal account >> user: $userId, request: $request." }
        return accountService.create(userId, request.toCommand(AccountType.PRIVATE)).toTO()
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteAccount(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ) {
        log.info { "Delete personal account >> user: $userId, accountId: $accountId." }
        accountService.delete(userId, AccountType.PRIVATE, accountId)
    }
}
