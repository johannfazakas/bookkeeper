package ro.jf.bk.account.domain.service

import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand

interface AccountRepository {
    fun findAll(): List<Account>
    fun save(command: CreateAccountCommand): Account
}