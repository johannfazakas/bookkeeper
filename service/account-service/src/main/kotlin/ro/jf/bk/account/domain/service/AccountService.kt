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
    fun find(userId: UUID, accountType: AccountType, accountId: UUID): Account? {
        return accountRepository.find(userId, accountType, accountId)
    }

    fun list(userId: UUID, accountType: AccountType): List<Account> {
        return accountRepository.findAllByType(userId, accountType)
    }

    fun create(userId: UUID, command: CreateAccountCommand): Account {
        return accountRepository.save(userId, command)
    }

    fun delete(userId: UUID, accountType: AccountType, accountId: UUID) {
        accountRepository.delete(userId, accountType, accountId)
    }
}
