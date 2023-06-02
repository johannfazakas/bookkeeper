package ro.jf.stuff.account.service

import org.springframework.stereotype.Service
import ro.jf.stuff.account.api.AccountService
import ro.jf.stuff.account.api.transfer.AccountTO
import ro.jf.stuff.account.api.transfer.CreateAccountTO

@Service
class AccountServiceImpl(): AccountService {

    override fun createAccount(createAccountTO: CreateAccountTO): AccountTO {
        TODO("Not yet implemented")
    }
}