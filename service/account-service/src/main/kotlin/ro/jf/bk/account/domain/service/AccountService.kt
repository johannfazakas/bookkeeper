package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun find(userId: UUID, accountId: UUID): Account? {
        return accountRepository.find(userId, accountId)
    }

    fun list(userId: UUID): List<Account> {
        return accountRepository.findAll(userId)
    }

    fun create(userId: UUID, command: CreateAccountCommand): Account {
        return accountRepository.save(userId, command)
    }

    fun delete(userId: UUID, accountId: UUID) {
        accountRepository.delete(userId, accountId)
    }
}
