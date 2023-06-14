package ro.jf.bk.api.api.account.service

import org.springframework.stereotype.Service
import ro.jf.bk.api.api.account.api.AccountService
import ro.jf.bk.api.api.account.api.model.AccountTO
import ro.jf.bk.api.api.account.api.model.AccountTO.Companion.toTO
import ro.jf.bk.api.api.account.api.model.CreateAccountTO
import ro.jf.bk.api.api.account.persistence.repository.AccountRepository

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
