package ro.jf.stuff.account.api

import ro.jf.stuff.account.api.transfer.AccountTO
import ro.jf.stuff.account.api.transfer.CreateAccountTO

interface AccountService {
    fun createAccount(createAccountTO: CreateAccountTO): AccountTO
}
