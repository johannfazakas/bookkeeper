package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun list(): List<Account> {
        return accountRepository.findAll()
    }

    fun create(command: CreateAccountCommand): Account {
        return accountRepository.save(command)
    }
}
