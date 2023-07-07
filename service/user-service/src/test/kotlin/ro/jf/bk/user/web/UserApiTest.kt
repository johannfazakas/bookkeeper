package ro.jf.bk.user.web

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
import ro.jf.bk.user.persistence.entity.UserEntity
import ro.jf.bk.user.persistence.repository.UserEntityRepository
import ro.jf.bk.user.web.transfer.CreateUserTO
import java.util.UUID.randomUUID

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
    private lateinit var userEntityRepository: UserEntityRepository

    private lateinit var client: WebTestClient

    @BeforeEach
    fun setUp() = runTest {
        userEntityRepository.deleteAll()
        client = WebTestClient
            .bindToApplicationContext(applicationContext)
            .configureClient()
            .build()
    }

    @Test
    fun `should list users`() = runTest {
        val userEntity1 = userEntityRepository.save(UserEntity(username = "user1"))
        val userEntity2 = userEntityRepository.save(UserEntity(username = "user2"))

        client.get()
            .uri("/user/v1/users")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.data").isArray
            .jsonPath("$.data.length()").isEqualTo(2)
            .jsonPath("$.data[0].id").isEqualTo(userEntity1.id.toString())
            .jsonPath("$.data[0].username").isEqualTo(userEntity1.username)
            .jsonPath("$.data[1].id").isEqualTo(userEntity2.id.toString())
            .jsonPath("$.data[1].username").isEqualTo(userEntity2.username)
    }

    @Test
    fun `should find user by`() = runTest {
        val username = "user"
        val userEntity = userEntityRepository.save(UserEntity(username = username))

        client.get()
            .uri("/user/v1/users/${userEntity.id}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(userEntity.id.toString())
            .jsonPath("$.username").isEqualTo(userEntity.username)
    }

    @Test
    fun `should return not found on find user by id when it does not exist`() = runTest {
        val userId = randomUUID()

        client.get()
            .uri("/user/v1/users/$userId")
            .exchange()
            .expectStatus().isNotFound
    }

    @Test
    fun `should find user by username`() = runTest {
        val username = "user"
        val userEntity = userEntityRepository.save(UserEntity(username = username))

        client.get()
            .uri("/user/v1/users/username/$username")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("$.id").isEqualTo(userEntity.id.toString())
            .jsonPath("$.username").isEqualTo(userEntity.username)
    }

    @Test
    fun `should return not found on find user by username when it does not exist`() = runTest {
        val username = "userx"

        client.get()
            .uri("/user/v1/users/username/$username")
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

        val user = userEntityRepository.findByUsername(username)
        assertThat(user).isNotNull
        assertThat(user?.id).isNotNull
        assertThat(user?.username).isEqualTo(username)
    }

    @Test
    fun `should throw error when attempting user creation with existing username`() = runTest {
        val existingUserEntity = userEntityRepository.save(UserEntity(username = "user"))

        client.post()
            .uri("/user/v1/users")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(CreateUserTO(existingUserEntity.username))
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value())
    }

    @Test
    fun `should delete user`() {
        val username = "user"
        runTest {
            val userEntity = userEntityRepository.save(UserEntity(username = username))

            client.delete()
                .uri("/user/v1/users/${userEntity.id}")
                .exchange()
                .expectStatus().isNoContent
            userEntityRepository.findByUsername(username).let {
                assertThat(it).isNull()
            }
        }
    }
}
