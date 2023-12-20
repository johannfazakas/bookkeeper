package ro.jf.bk.account.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import ro.jf.bk.account.api.transfer.CreateAccountTO
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.extension.UserMockIntegrationExtension
import ro.jf.bk.account.extension.UserMockIntegrationExtension.Companion.injectUserIntegrationProps
import ro.jf.bk.account.extension.givenExistingUser
import ro.jf.bk.account.extension.userIdHeader
import ro.jf.bk.account.persistence.entity.AccountEntity
import ro.jf.bk.account.persistence.repository.AccountEntityRepository
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(
    PostgresContainerExtension::class,
    UserMockIntegrationExtension::class
)
class IncomeSourceApiTest {
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
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        accountEntityRepository.deleteAll()
    }

    @Test
    fun `should get income source`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val accountEntity = accountEntityRepository.save(
            AccountEntity(null, userId, "income-1", AccountType.INCOME_SOURCE.value, "RON")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/account/v1/income-sources/{id}", accountEntity.id)
                .userIdHeader(userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountEntity.id.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(accountEntity.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(accountEntity.currency))
    }

    @Test
    fun `should list accounts`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val incomeSource1 = accountEntityRepository.save(
            AccountEntity(null, userId, "account-1", AccountType.INCOME_SOURCE.value, "RON")
        )
        val incomeSource2 = accountEntityRepository.save(
            AccountEntity(null, userId, "account-2", AccountType.INCOME_SOURCE.value, "EUR")
        )
        accountEntityRepository.save(
            AccountEntity(null, UUID.randomUUID(), "account-3", AccountType.INCOME_SOURCE.value, "EUR")
        )
        accountEntityRepository.save(
            AccountEntity(null, userId, "account-4", AccountType.PRIVATE.value, "EUR")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.get("/account/v1/income-sources")
                .userIdHeader(userId)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize<Any>(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(incomeSource1.id.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].user_id").value(userId.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(incomeSource1.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].currency").value(incomeSource1.currency))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].id").value(incomeSource2.id.toString()))
    }

    @Test
    fun `should create income source`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val request = CreateAccountTO("salary", "USD")

        mockMvc.perform(
            MockMvcRequestBuilders.post("/account/v1/income-sources")
                .userIdHeader(userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(MockMvcResultMatchers.status().isCreated)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
            .andExpect(MockMvcResultMatchers.jsonPath("$.user_id").value(userId.toString()))
            .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(request.name))
            .andExpect(MockMvcResultMatchers.jsonPath("$.currency").value(request.currency))

        val account = accountEntityRepository
            .findByUserIdAndTypeAndName(userId, AccountType.INCOME_SOURCE.value, request.name)

        Assertions.assertThat(account).isNotNull
        Assertions.assertThat(account!!.userId).isEqualTo(userId)
        Assertions.assertThat(account.currency).isEqualTo(request.currency)
    }

    @Test
    fun `should delete account`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val incomeSource = accountEntityRepository.save(
            AccountEntity(null, userId, "account-1", AccountType.INCOME_SOURCE.value, "RON")
        )

        mockMvc.perform(
            MockMvcRequestBuilders.delete("/account/v1/income-sources/{id}", incomeSource.id)
                .userIdHeader(userId)
        )
            .andExpect(MockMvcResultMatchers.status().isNoContent)

        accountEntityRepository.findById(incomeSource.id!!).isEmpty
    }
}
