package ro.jf.stuff.account.api

import ro.jf.stuff.account.api.model.AccountTO
import ro.jf.stuff.account.api.model.CreateAccountTO

interface AccountService {
    fun createAccount(createAccountTO: CreateAccountTO): AccountTO
    fun getAccounts(): List<AccountTO>
}
