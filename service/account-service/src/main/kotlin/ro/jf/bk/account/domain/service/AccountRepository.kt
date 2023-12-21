package ro.jf.bk.account.domain.service

import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

interface AccountRepository {
    fun find(userId: UUID, accountType: AccountType, accountId: UUID): Account?
    fun findAllByType(userId: UUID, accountType: AccountType): List<Account>
    fun findAll(userId: UUID): List<Account>
    fun save(userId: UUID, command: CreateAccountCommand): Account
    fun delete(userId: UUID, accountType: AccountType, accountId: UUID)
}
