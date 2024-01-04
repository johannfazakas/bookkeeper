package ro.jf.bk.account.domain.service

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.command.CreateAccountCommand
import java.util.UUID.randomUUID

class AccountServiceTest {

    private val accountRepository = mock<AccountRepository>()
    private val accountService = AccountService(accountRepository)

    @Test
    fun shouldGetAccount() {
        val userId = randomUUID()
        val accountId = randomUUID()
        val accountType = AccountType.PRIVATE
        val account = Account(accountId, userId, "account-name",  AccountType.PRIVATE, "RON", "ref")
        whenever(accountRepository.find(userId, accountType, accountId)).thenReturn(account)

        val retrievedAccount = accountService.getById(userId, accountType, accountId)

        assertThat(retrievedAccount).isNotNull()
        assertThat(retrievedAccount?.id).isEqualTo(accountId)
        assertThat(retrievedAccount?.userId).isEqualTo(userId)
        assertThat(retrievedAccount?.name).isEqualTo(account.name)
        assertThat(retrievedAccount?.currency).isEqualTo(account.currency)
    }

    @Test
    fun shouldRetrieveAccounts() {
        val userId = randomUUID()
        val accountType = AccountType.PRIVATE
        val account1 = Account(randomUUID(), userId, "account-name-1", AccountType.PRIVATE, "RON", "ref-1")
        val account2 = Account(randomUUID(), userId, "account-name-2", AccountType.PRIVATE, "EUR", "ref-2")
        whenever(accountRepository.findAllByType(userId, accountType)).thenReturn(listOf(account1, account2))

        val accounts = accountService.findByType(userId, accountType)

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
        val type = AccountType.PRIVATE
        val createCommand = CreateAccountCommand("account-name", type, "RON", "ref")
        whenever(accountRepository.save(eq(userId), any<CreateAccountCommand>()))
            .thenAnswer {
                it.getArgument<CreateAccountCommand>(1).let { command ->
                    Account(accountId, userId, command.name, command.type, command.currency, command.externalReference)
                }
            }

        val account = accountService.create(userId, createCommand)

        assertThat(account.id).isEqualTo(accountId)
        assertThat(account.userId).isEqualTo(userId)
        assertThat(account.name).isEqualTo(createCommand.name)
        assertThat(account.currency).isEqualTo(createCommand.currency)
    }

    @Test
    fun shouldDeleteAccount() {
        val userId = randomUUID()
        val accountId = randomUUID()
        val type = AccountType.PRIVATE

        accountService.delete(userId, type, accountId)

        verify(accountRepository).delete(userId, type, accountId)
    }
}
