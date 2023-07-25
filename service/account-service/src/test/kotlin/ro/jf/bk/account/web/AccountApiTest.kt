package ro.jf.bk.account.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.extension.UserMockIntegrationExtension
import ro.jf.bk.account.extension.UserMockIntegrationExtension.Companion.injectUserIntegrationProps
import ro.jf.bk.account.extension.givenExistingUser
import ro.jf.bk.account.extension.givenNonExistingUser
import ro.jf.bk.account.persistence.entity.AccountEntity
import ro.jf.bk.account.persistence.repository.AccountEntityRepository
import ro.jf.bk.account.web.transfer.CreateAccountTO
import java.util.*
import java.util.UUID.randomUUID

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(
    PostgresContainerExtension::class,
    UserMockIntegrationExtension::class
)
class AccountApiTest {
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
    fun `should get account`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val accountEntity = accountEntityRepository.save(AccountEntity(null, userId, "account-1", "RON"))

        mockMvc.perform(
            get("/account/v1/accounts/{id}", accountEntity.id)
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(accountEntity.id.toString()))
            .andExpect(jsonPath("$.user_id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value(accountEntity.name))
            .andExpect(jsonPath("$.currency").value(accountEntity.currency))
    }

    @Test
    fun `should list accounts`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val accountEntity1 = accountEntityRepository.save(AccountEntity(null, userId, "account-1", "RON"))
        val accountEntity2 = accountEntityRepository.save(AccountEntity(null, userId, "account-2", "EUR"))
        accountEntityRepository.save(AccountEntity(null, randomUUID(), "account-2", "EUR"))

        mockMvc.perform(
            get("/account/v1/accounts")
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data", hasSize<Any>(2)))
            .andExpect(jsonPath("$.data[0].id").value(accountEntity1.id.toString()))
            .andExpect(jsonPath("$.data[0].user_id").value(userId.toString()))
            .andExpect(jsonPath("$.data[0].name").value(accountEntity1.name))
            .andExpect(jsonPath("$.data[0].currency").value(accountEntity1.currency))
            .andExpect(jsonPath("$.data[1].id").value(accountEntity2.id.toString()))
    }

    @Test
    fun `should create account`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val request = CreateAccountTO("account-name", "USD")

        mockMvc.perform(
            post("/account/v1/accounts")
                .userIdHeader(userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.user_id").value(userId.toString()))
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.currency").value(request.currency))

        val account = accountEntityRepository.findByUserIdAndName(userId, request.name)

        assertThat(account).isNotNull
        assertThat(account!!.userId).isEqualTo(userId)
        assertThat(account!!.currency).isEqualTo(request.currency)
    }

    @Test
    fun `should delete account`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenExistingUser(userId)
        val accountEntity = accountEntityRepository.save(AccountEntity(null, userId, "account-1", "RON"))

        mockMvc.perform(
            delete("/account/v1/accounts/{id}", accountEntity.id)
                .userIdHeader(userId)
        )
            .andExpect(status().isNoContent)

        accountEntityRepository.findById(accountEntity.id!!).isEmpty
    }

    @Test
    fun `should return unauthorized when user id is not sent`() {
        mockMvc.perform(get("/account/v1/accounts"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `should return unauthorized when not found user id id sent`(mockServerClient: MockServerClient) {
        val userId = randomUUID()
        mockServerClient.givenNonExistingUser(userId)
        mockMvc.perform(get("/account/v1/accounts").userIdHeader(userId))
            .andExpect(status().isUnauthorized)
    }

    private fun MockHttpServletRequestBuilder.userIdHeader(userId: UUID) =
        this.header("BK_USER_ID", userId)
}
