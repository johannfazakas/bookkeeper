package ro.jf.bk.api.api.account.api

import ro.jf.bk.api.api.account.api.model.AccountTO
import ro.jf.bk.api.api.account.api.model.CreateAccountTO

interface AccountService {
    fun createAccount(createAccountTO: CreateAccountTO): AccountTO
    fun getAccounts(): List<AccountTO>
}
