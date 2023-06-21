package ro.jf.bk.account.controller

import org.springframework.web.bind.annotation.*
import ro.jf.bk.account.api.AccountService
import ro.jf.bk.account.api.model.AccountTO
import ro.jf.bk.account.api.model.CreateAccountTO

@RestController
@RequestMapping("/account/v1/accounts")
class AccountController(
        private val accountService: AccountService
) {
    @GetMapping
    fun getAccounts(): List<AccountTO> {
        return accountService.getAccounts()
    }

    @PostMapping
    fun createAccount(@RequestBody request: CreateAccountTO): AccountTO {
        return accountService.createAccount(request)
    }
}
