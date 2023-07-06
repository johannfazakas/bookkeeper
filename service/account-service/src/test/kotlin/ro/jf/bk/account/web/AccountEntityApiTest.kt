package ro.jf.bk.account.web

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.hamcrest.Matchers.hasSize
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ro.jf.bk.account.web.transfer.CreateAccountTO
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.persistence.entity.AccountEntity
import ro.jf.bk.account.persistence.repository.AccountEntityRepository

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresContainerExtension::class)
class AccountEntityApiTest {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.injectPostgresConnectionProps()
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
    fun `should get accounts`() {
        val accountEntity1 = accountEntityRepository.save(AccountEntity(null, "account-1", "RON"))
        val accountEntity2 = accountEntityRepository.save(AccountEntity(null, "account-2", "EUR"))

        mockMvc.perform(get("/account/v1/accounts"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data", hasSize<Any>(2)))
            .andExpect(jsonPath("$.data[0].id").value(accountEntity1.id.toString()))
            .andExpect(jsonPath("$.data[0].name").value(accountEntity1.name))
            .andExpect(jsonPath("$.data[0].currency").value(accountEntity1.currency))
            .andExpect(jsonPath("$.data[1].id").value(accountEntity2.id.toString()))
    }

    @Test
    fun `should create account`() {
        val request = CreateAccountTO("account-name", "USD")

        mockMvc.perform(
            post("/account/v1/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.name").value(request.name))
            .andExpect(jsonPath("$.currency").value(request.currency))

        val account = accountEntityRepository.findByName(request.name)
        assertThat(account).isNotNull
        assertThat(account!!.currency).isEqualTo(request.currency)
    }

}