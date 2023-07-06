package ro.jf.bk.account.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import ro.jf.bk.account.domain.service.AccountService
import ro.jf.bk.account.web.transfer.CreateAccountTO
import ro.jf.bk.account.persistence.model.Account
import ro.jf.bk.account.persistence.repository.AccountRepository
import java.util.UUID.randomUUID

class AccountServiceTest {

    private val accountRepository = mock(AccountRepository::class.java)
    private val accountService = AccountService(accountRepository)

    @Test
    fun shouldRetrieveAccounts() {
        val account1 = Account(randomUUID(), "account-name-1", "RON")
        val account2 = Account(randomUUID(), "account-name-2", "EUR")
        `when`(accountRepository.findAll()).thenReturn(listOf(account1, account2))

        val accounts = accountService.getAccounts()

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

        val account = accountService.createAccount(createAccountTO)

        assertThat(account.id).isEqualTo(accountId)
        assertThat(account.name).isEqualTo(createAccountTO.name)
        assertThat(account.currency).isEqualTo(createAccountTO.currency)
    }
}
