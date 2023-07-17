package ro.jf.bk.account.domain.service

import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

interface AccountRepository {
    fun find(userId: UUID, accountId: UUID): Account?
    fun findAll(userId: UUID): List<Account>
    fun save(command: CreateAccountCommand): Account
    fun delete(userId: UUID, accountId: UUID)
}
