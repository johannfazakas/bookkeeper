package ro.jf.bk.account.web

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.domain.service.AccountService
import ro.jf.bk.account.web.interceptor.USER_ID_HEADER_KEY
import ro.jf.bk.account.web.transfer.AccountTO
import ro.jf.bk.account.web.transfer.AccountTO.Companion.toTO
import ro.jf.bk.account.web.transfer.CreateAccountTO
import ro.jf.bk.account.web.transfer.ListTO
import ro.jf.bk.account.web.transfer.ListTO.Companion.toListTO
import java.util.*

@RestController
@RequestMapping("/account/v1/accounts")
class AccountController(
    private val accountService: AccountService
) {
    @GetMapping
    fun getAccounts(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID
    ): ListTO<AccountTO> {
        return accountService.list(userId).map { it.toTO() }.toListTO()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(
        @RequestHeader(USER_ID_HEADER_KEY) userId: UUID,
        @RequestBody request: CreateAccountTO
    ): AccountTO {
        return accountService.create(request.toCommand()).toTO()
    }
}
