package ro.jf.bk.account.api.web

import mu.KotlinLogging.logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.api.transfer.*
import ro.jf.bk.account.api.web.error.AccountNotFoundException
import ro.jf.bk.account.api.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.service.AccountService
import java.util.*

private val log = logger { }

@RestController
@RequestMapping("/account/v1/income-sources")
class IncomeSourceController(
    private val accountService: AccountService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createIncomeSource(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestBody request: CreateAccountTO
    ): AccountTO {
        log.info { "Create income source >> user: $userId, request: $request." }
        return accountService.create(userId, request.toCommand(AccountType.INCOME_SOURCE)).toTO()
    }

    @GetMapping
    fun listIncomeSources(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID
    ): ListTO<AccountTO> {
        log.debug { "List income sources >> user: $userId." }
        return accountService.findByType(userId, AccountType.INCOME_SOURCE).map(Account::toTO).toListTO()
    }

    @GetMapping("/{accountId}")
    fun getIncomeSource(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ): AccountTO {
        log.debug { "Get income source >> user: $userId, accountId: $accountId." }
        return accountService.getById(userId, AccountType.INCOME_SOURCE, accountId)?.toTO()
            ?: throw AccountNotFoundException(userId, accountId)
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteIncomeSource(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ) {
        log.info { "Delete income source >> user: $userId, accountId: $accountId." }
        accountService.delete(userId, AccountType.INCOME_SOURCE, accountId)
    }
}
