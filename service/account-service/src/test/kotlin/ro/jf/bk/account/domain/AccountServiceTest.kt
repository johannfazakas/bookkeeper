package ro.jf.bk.account.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import ro.jf.bk.account.domain.service.AccountRepository
import ro.jf.bk.account.domain.service.AccountService
import java.util.UUID.randomUUID

class AccountServiceTest {

    private val accountRepository = mock<AccountRepository>()
    private val accountService = AccountService(accountRepository)

    @Test
    fun shouldRetrieveAccounts() {
        val userId = randomUUID()
        val account1 = Account(randomUUID(), userId, "account-name-1", "RON")
        val account2 = Account(randomUUID(), userId, "account-name-2", "EUR")
        whenever(accountRepository.findAll(userId)).thenReturn(listOf(account1, account2))

        val accounts = accountService.list(userId)

        assertThat(accounts).hasSize(2)
        assertThat(accounts[0].id).isEqualTo(account1.id)
        assertThat(accounts[0].userId).isEqualTo(userId)
        assertThat(accounts[0].name).isEqualTo(account1.name)
        assertThat(accounts[0].currency).isEqualTo(account1.currency)
        assertThat(accounts[1].id).isEqualTo(account2.id)
    }

    @Test
    fun shouldCreateAccount() {
        val userId = randomUUID()
        val accountId = randomUUID()
        val createCommand = CreateAccountCommand(userId, "account-name", "RON")
        whenever(accountRepository.save(any<CreateAccountCommand>()))
            .thenAnswer {
                it.getArgument<CreateAccountCommand>(0).let { command ->
                    Account(accountId, userId, command.name, command.currency)
                }
            }

        val account = accountService.create(createCommand)

        assertThat(account.id).isEqualTo(accountId)
        assertThat(account.userId).isEqualTo(userId)
        assertThat(account.name).isEqualTo(createCommand.name)
        assertThat(account.currency).isEqualTo(createCommand.currency)
    }
}
