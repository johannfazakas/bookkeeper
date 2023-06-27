package ro.jf.bk.account.api

import ro.jf.bk.account.api.model.AccountTO
import ro.jf.bk.account.api.model.CreateAccountTO

interface AccountService {
    fun createAccount(createAccountTO: CreateAccountTO): AccountTO
    fun getAccounts(): List<AccountTO>
}
