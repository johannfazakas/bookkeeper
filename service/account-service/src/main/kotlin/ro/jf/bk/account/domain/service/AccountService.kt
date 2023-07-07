package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import java.util.*

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun list(userId: UUID): List<Account> {
        return accountRepository.findAll(userId)
    }

    fun create(command: CreateAccountCommand): Account {
        return accountRepository.save(command)
    }
}
