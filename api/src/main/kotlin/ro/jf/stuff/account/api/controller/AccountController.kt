package ro.jf.stuff.account.api.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/account/v1/accounts")
class AccountController {

    @PostMapping
    fun createAccount(): String {
        return "Hello"
    }
}
