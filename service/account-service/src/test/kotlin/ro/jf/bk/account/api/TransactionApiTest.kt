package ro.jf.bk.account.api

import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.extension.UserMockIntegrationExtension
import ro.jf.bk.account.extension.UserMockIntegrationExtension.Companion.injectUserIntegrationProps
import ro.jf.bk.account.extension.givenExistingUser
import ro.jf.bk.account.extension.userIdHeader
import ro.jf.bk.account.infrastructure.persistence.entity.AccountEntity
import ro.jf.bk.account.infrastructure.persistence.entity.TransactionEntity
import ro.jf.bk.account.infrastructure.persistence.repository.AccountEntityRepository
import ro.jf.bk.account.infrastructure.persistence.repository.TransactionEntityRepository
import java.math.BigDecimal
import java.time.Instant
import java.util.*
import java.util.UUID.randomUUID

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

    @BeforeEach
    fun setUp() {
        transactionEntityRepository.deleteAll()
        accountEntityRepository.deleteAll()
    }

    @Test
    fun `should create transaction`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val account1 = accountEntityRepository.save(
            AccountEntity(null, userId, "account-1", AccountType.PRIVATE.value, "RON", "ref-1")
        )
        val account2 = accountEntityRepository.save(
            AccountEntity(null, userId, "account-2", AccountType.PRIVATE.value, "RON", "ref-2")
        )

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
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val account1 =
            accountEntityRepository.save(
                AccountEntity(
                    null,
                    userId,
                    "account-1",
                    AccountType.PRIVATE.value,
                    "RON",
                    "ref-1"
                )
            )
        val account2 =
            accountEntityRepository.save(
                AccountEntity(
                    null,
                    userId,
                    "account-2",
                    AccountType.PRIVATE.value,
                    "RON",
                    "ref-2"
                )
            )
        val transaction = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), account1.id!!, account2.id!!, BigDecimal(100.25), "test")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/account/v1/transactions/${transaction.id}")
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(transaction.id.toString()))
            .andExpect(jsonPath("$.userId").value(userId.toString()))
            .andExpect(jsonPath("$.from").value(account1.id.toString()))
            .andExpect(jsonPath("$.to").value(account2.id.toString()))
            .andExpect(jsonPath("$.amount").value("100.25"))
            .andExpect(jsonPath("$.description").value("test"))
    }

    @Test
    fun `should return 404 on get transaction when not found`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)

        mockMvc.perform(
            MockMvcRequestBuilders.get("/account/v1/transactions/${randomUUID()}")
                .userIdHeader(userId)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.title").value("Transaction not found."))
    }

    @Test
    fun `should list transactions by account`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val account1Id = createAccount(userId, "account-1", AccountType.PRIVATE, "RON")
        val account2Id = createAccount(userId, "account-2", AccountType.PRIVATE, "RON")
        val account3Id = createAccount(userId, "account-3", AccountType.PRIVATE, "RON")
        val transaction1 = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), account1Id, account2Id, BigDecimal(100.25), "test")
        )
        val transaction2 = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), account2Id, account1Id, BigDecimal(100.25), "test")
        )
        val transaction3 = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), account1Id, account2Id, BigDecimal(100.25), "test")
        )
        val transaction4 = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), account3Id, account1Id, BigDecimal(100.25), "test")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/account/v1/transactions?accountId=${account2Id}")
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data", hasSize<Any>(3)))
            .andExpect(jsonPath("$.data[2].id").value(transaction1.id.toString()))
            .andExpect(jsonPath("$.data[1].id").value(transaction2.id.toString()))
            .andExpect(jsonPath("$.data[0].id").value(transaction3.id.toString()))
    }

    @Test
    fun `should delete transaction`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val accountId = createAccount(userId, "account-1", AccountType.PRIVATE, "RON")
        val transaction1 = transactionEntityRepository.save(
            TransactionEntity(null, userId, Instant.now(), accountId, accountId, BigDecimal(100.25), "test")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/account/v1/transactions/${transaction1.id}")
                .userIdHeader(userId)
        )
            .andExpect(status().isNoContent)

        assertThat(transactionEntityRepository.findByUserIdAndId(userId, transaction1.id!!)).isNull()
    }

    @Test
    fun `should import wallet csv transactions`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        listOf("Cash", "Revolut", "BT", "Other").forEach {
            createAccount(userId, "$it account", AccountType.PRIVATE, "RON", it)
        }
        listOf("Work Income").forEach {
            createAccount(userId, "$it account", AccountType.INCOME_SOURCE, "RON", it)
        }
        listOf("Food", "Home", "Gifts", "Education", "Car & Transport", "Shopping").forEach {
            createAccount(userId, "$it account", AccountType.EXPENSE_TARGET, "RON", it)
        }


        val mockMultipartFile = ClassPathResource("mock/wallet.csv")
            .let { MockMultipartFile("file", it.filename, "text/csv", it.inputStream.readBytes()) }
        mockMvc.perform(
            MockMvcRequestBuilders.multipart(HttpMethod.PUT, "/account/v1/transactions/import/wallet-csv")
                .file(mockMultipartFile)
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
    }

    private fun createAccount(
        userId: UUID, name: String, type: AccountType, currency: String, externalReference: String? = null
    ): UUID =
        accountEntityRepository.save(AccountEntity(null, userId, name, type.value, currency, externalReference)).id!!
}
