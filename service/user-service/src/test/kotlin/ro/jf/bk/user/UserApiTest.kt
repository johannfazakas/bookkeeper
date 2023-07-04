package ro.jf.bk.user

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import ro.jf.bk.user.extension.PostgresContainerExtension
import ro.jf.bk.user.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.user.model.User
import ro.jf.bk.user.repository.UserRepository
import ro.jf.bk.user.transfer.CreateUserTO

@SpringBootTest
@ExtendWith(PostgresContainerExtension::class)
@OptIn(ExperimentalCoroutinesApi::class)
class UserApiTest(
) {
    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun properties(registry: DynamicPropertyRegistry) {
            registry.injectPostgresConnectionProps()
        }
    }

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() = runTest {
        userRepository.deleteAll()
        client = WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .build()
    }

    @Test
    fun `should get users`() = runTest {
        val user1 = userRepository.save(User(username = "user1"))
        val user2 = userRepository.save(User(username = "user2"))

        client.get()
            .uri("/user/v1/users")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data.length()").isEqualTo(2)
            .jsonPath("$.data[0].id").isEqualTo(user1.id.toString())
            .jsonPath("$.data[0].username").isEqualTo(user1.username)
            .jsonPath("$.data[1].id").isEqualTo(user2.id.toString())
            .jsonPath("$.data[1].username").isEqualTo(user2.username)
    }

    @Test
    fun `should get user`() = runTest {
        val username = "user"
        val user = User(username = username)
        userRepository.save(user)

        client.get()
            .uri("/user/v1/users/$username")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(user.id.toString())
            .jsonPath("$.username").isEqualTo(user.username)
    }

    @Test
    fun `should return not found on get user when it does not exist`() = runTest {
        val username = "userx"

        client.get()
            .uri("/user/v1/users/$username")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should create user`() = runTest {
        val username = "user"

        client.post()
            .uri("/user/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CreateUserTO(username))
            .exchange()
            .expectStatus().isCreated
            .expectBody()
            .jsonPath("$.id").exists()
            .jsonPath("$.username").isEqualTo(username)

        val user = userRepository.findByUsername(username)
        assertThat(user).isNotNull
        assertThat(user?.id).isNotNull
        assertThat(user?.username).isEqualTo(username)
    }

    @Test
    fun `should throw error when attempting user creation with existing username`() = runTest {
        val existingUser = userRepository.save(User(username = "user"))

        client.post()
            .uri("/user/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CreateUserTO(existingUser.username))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    @Test
    fun `should delete user`() = runTest {
        val user = userRepository.save(User(username = "user"))
        val count = userRepository.findAll()

        client.delete()
            .uri("/user/v1/users/${user.username}")
            .exchange()
            .expectStatus().isNoContent
    }
}
