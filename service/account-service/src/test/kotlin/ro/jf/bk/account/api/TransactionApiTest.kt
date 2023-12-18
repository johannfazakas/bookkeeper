package ro.jf.bk.account.api

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.extension.UserMockIntegrationExtension
import ro.jf.bk.account.extension.UserMockIntegrationExtension.Companion.injectUserIntegrationProps
import ro.jf.bk.account.extension.givenExistingUser
import ro.jf.bk.account.extension.userIdHeader
import ro.jf.bk.account.persistence.entity.AccountEntity
import ro.jf.bk.account.persistence.repository.AccountEntityRepository
import ro.jf.bk.account.persistence.repository.TransactionEntityRepository
import java.math.BigDecimal
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(
    PostgresContainerExtension::class,
    UserMockIntegrationExtension::class
)
class TransactionApiTest {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.injectPostgresConnectionProps()
            registry.injectUserIntegrationProps()
        }
    }

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var accountEntityRepository: AccountEntityRepository

    @Autowired
    private lateinit var transactionEntityRepository: TransactionEntityRepository

    @Test
    fun `should create transaction`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val account1 = accountEntityRepository.save(AccountEntity(null, userId, "account-1", "RON"))
        val account2 = accountEntityRepository.save(AccountEntity(null, userId, "account-2", "RON"))

        mockMvc.perform(
            MockMvcRequestBuilders.post("/account/v1/transactions")
                .userIdHeader(userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    """
                    {
                        "timestamp": "2021-01-01T00:00:00.000Z",
                        "from": "${account1.id}",
                        "to": "${account2.id}",
                        "amount": 100.25,
                        "description": "test"
                    }
                    """.trimIndent()
                )
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").exists())

        val transactions = transactionEntityRepository.findByUserIdAndAccount(userId, account1.id!!)

        assertThat(transactions).hasSize(1)
        assertThat(transactions[0].from).isEqualTo(account1.id)
        assertThat(transactions[0].to).isEqualTo(account2.id)
        assertThat(transactions[0].amount).isEqualTo(BigDecimal(100.25))
    }

    @Test
    fun `should get transaction`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        // TODO
    }

    @Test
    fun `should list transactions by account`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        // TODO
    }

    @Test
    fun `should delete transaction`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        // TODO
    }
}
