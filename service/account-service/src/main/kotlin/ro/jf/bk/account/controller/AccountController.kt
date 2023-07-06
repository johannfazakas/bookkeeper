package ro.jf.bk.account.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.api.AccountService
import ro.jf.bk.account.api.model.AccountTO
import ro.jf.bk.account.api.model.CreateAccountTO
import ro.jf.bk.account.api.model.ListTO
import ro.jf.bk.account.api.model.ListTO.Companion.toListTO

@RestController
@RequestMapping("/account/v1/accounts")
class AccountController(
    private val accountService: AccountService
) {
    @GetMapping
    fun getAccounts(): ListTO<AccountTO> {
        return accountService.getAccounts().toListTO()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createAccount(@RequestBody request: CreateAccountTO): AccountTO {
        return accountService.createAccount(request)
    }
}
