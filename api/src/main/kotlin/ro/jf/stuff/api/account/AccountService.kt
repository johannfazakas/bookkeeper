package ro.jf.stuff.api.account

import org.springframework.stereotype.Service
import ro.jf.stuff.api.account.api.model.Account

@Service
class AccountService() {
    fun createAccount(): Account {
        return Account("EUR")
    }
}