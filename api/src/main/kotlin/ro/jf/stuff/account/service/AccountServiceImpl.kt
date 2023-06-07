package ro.jf.stuff.account.service

import org.springframework.stereotype.Service
import ro.jf.stuff.account.api.AccountService
import ro.jf.stuff.account.api.model.AccountTO
import ro.jf.stuff.account.api.model.AccountTO.Companion.toTO
import ro.jf.stuff.account.api.model.CreateAccountTO
import ro.jf.stuff.account.persistence.AccountRepository

@Service
class AccountServiceImpl(
        private val accountRepository: AccountRepository
) : AccountService {
    override fun getAccounts(): List<AccountTO> {
        return accountRepository.findAll().map { it.toTO() }
    }

    override fun createAccount(createAccountTO: CreateAccountTO): AccountTO {
        return accountRepository.save(createAccountTO.toDomain()).toTO()
    }
}
