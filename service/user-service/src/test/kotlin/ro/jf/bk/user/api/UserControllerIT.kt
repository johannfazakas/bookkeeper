package ro.jf.bk.user.api

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
import ro.jf.bk.user.api.transfer.CreateUserTO
import ro.jf.bk.user.extension.PostgresContainerExtension
import ro.jf.bk.user.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.user.model.User
import ro.jf.bk.user.repository.UserRepository

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(PostgresContainerExtension::class)
class UserControllerIT(
) {
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
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
    }

    @Test
    fun `should get users`() {
        val user1 = userRepository.save(User(username = "user1"))
        val user2 = userRepository.save(User(username = "user2"))

        mockMvc.perform(get("/user/v1/users"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.data", hasSize<Any>(2)))
            .andExpect(jsonPath("$.data[0].id").value(user1.id.toString()))
            .andExpect(jsonPath("$.data[0].username").value(user1.username))
            .andExpect(jsonPath("$.data[1].id").value(user2.id.toString()))
            .andExpect(jsonPath("$.data[1].username").value(user2.username))
    }

    @Test
    fun `should get user`() {
        val username = "user"
        val user = User(username = username)
        userRepository.save(user)

        mockMvc.perform(get("/user/v1/users/$username"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(user.id.toString()))
            .andExpect(jsonPath("$.username").value(username))
    }

    @Test
    fun `should return not found on get user when it does not exist`() {
        val username = "userx"

        mockMvc.perform(get("/user/v1/users/$username"))
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should create user`() {
        val username = "user"

        mockMvc.perform(
            post("/user/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateUserTO(username)))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.username").value(username))

        val user = userRepository.findByUsername(username)
        assertThat(user).isNotNull
        assertThat(user?.id).isNotNull
        assertThat(user?.username).isEqualTo(username)
    }

    @Test
    fun `should throw error when attempting user creating with existing username`() {
        val existingUser = userRepository.save(User(username = "user"))

        mockMvc.perform(
            post("/user/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(CreateUserTO(existingUser.username)))
        )
            .andExpect(status().isUnprocessableEntity)
    }

    @Test
    fun `should delete user`() {
        val user = userRepository.save(User(username = "user"))

        mockMvc.perform(delete("/user/v1/users/${user.username}"))
            .andExpect(status().isNoContent)

        assertThat(userRepository.findByUsername(user.username)).isNull()
    }
}
