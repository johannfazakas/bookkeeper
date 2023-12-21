package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun getById(userId: UUID, accountType: AccountType, accountId: UUID): Account? {
        return accountRepository.find(userId, accountType, accountId)
    }

    fun findByType(userId: UUID, accountType: AccountType): List<Account> {
        return accountRepository.findAllByType(userId, accountType)
    }

    fun findAll(userId: UUID): List<Account> {
        return accountRepository.findAll(userId)
    }

    fun create(userId: UUID, command: CreateAccountCommand): Account {
        return accountRepository.save(userId, command)
    }

    fun delete(userId: UUID, accountType: AccountType, accountId: UUID) {
        accountRepository.delete(userId, accountType, accountId)
    }
}
