package ro.jf.bk.account.domain.service

import org.springframework.stereotype.Service
import ro.jf.bk.account.persistence.repository.AccountRepository
import ro.jf.bk.account.web.transfer.AccountTO
import ro.jf.bk.account.web.transfer.AccountTO.Companion.toTO
import ro.jf.bk.account.web.transfer.CreateAccountTO

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {
    fun getAccounts(): List<AccountTO> {
        return accountRepository.findAll().map { it.toTO() }
    }

    fun createAccount(createAccountTO: CreateAccountTO): AccountTO {
        return accountRepository.save(createAccountTO.toDomain()).toTO()
    }
}
