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
@RequestMapping("/account/v1/expense-targets")
class ExpenseTargetController(
    private val accountService: AccountService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createExpenseTarget(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestBody request: CreateAccountTO
    ): AccountTO {
        log.info { "Create expense target >> user: $userId, request: $request." }
        return accountService.create(userId, request.toCommand(AccountType.EXPENSE_TARGET)).toTO()
    }

    @GetMapping
    fun listExpenseTargets(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID
    ): ListTO<AccountTO> {
        log.debug { "List expense targets >> user: $userId." }
        return accountService.findByType(userId, AccountType.EXPENSE_TARGET).map(Account::toTO).toListTO()
    }

    @GetMapping("/{accountId}")
    fun getExpenseTarget(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ): AccountTO {
        log.debug { "Get expense target >> user: $userId, accountId: $accountId." }
        return accountService.getById(userId, AccountType.EXPENSE_TARGET, accountId)?.toTO()
            ?: throw AccountNotFoundException(userId, accountId)
    }

    @DeleteMapping("/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteExpenseTarget(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @PathVariable("accountId") accountId: UUID
    ) {
        log.info { "Delete expense target >> user: $userId, accountId: $accountId." }
        accountService.delete(userId, AccountType.EXPENSE_TARGET, accountId)
    }
}
