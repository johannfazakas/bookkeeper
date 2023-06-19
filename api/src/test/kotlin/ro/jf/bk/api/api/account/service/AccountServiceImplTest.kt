package ro.jf.bk.api.api.account.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import ro.jf.bk.api.api.account.api.model.CreateAccountTO
import ro.jf.bk.api.api.account.persistence.entity.Account
import ro.jf.bk.api.api.account.persistence.repository.AccountRepository
import java.util.UUID.randomUUID

class AccountServiceImplTest {

    private val accountRepository = mock(AccountRepository::class.java)
    private val accountServiceImpl = AccountServiceImpl(accountRepository)

    @Test
    fun shouldRetrieveAccounts() {
        val account1 = Account(randomUUID(), "account-name-1", "RON")
        val account2 = Account(randomUUID(), "account-name-2", "EUR")
        `when`(accountRepository.findAll()).thenReturn(listOf(account1, account2))

        val accounts = accountServiceImpl.getAccounts()

        assertThat(accounts).hasSize(2)
        assertThat(accounts[0].id).isEqualTo(account1.id)
        assertThat(accounts[0].name).isEqualTo(account1.name)
        assertThat(accounts[0].currency).isEqualTo(account1.currency)
    }

    @Test
    fun shouldCreateAccount() {
        val accountId = randomUUID()
        `when`(accountRepository.save(any(Account::class.java)))
                .thenAnswer { it.getArgument<Account>(0).apply { id = accountId } }
        val createAccountTO = CreateAccountTO("account-name", "RON")

        val account = accountServiceImpl.createAccount(createAccountTO)

        assertThat(account.id).isEqualTo(accountId)
        assertThat(account.name).isEqualTo(createAccountTO.name)
        assertThat(account.currency).isEqualTo(createAccountTO.currency)
    }
}