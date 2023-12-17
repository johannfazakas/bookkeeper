package ro.jf.bk.account.api

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpMethod
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import ro.jf.bk.account.extension.PostgresContainerExtension
import ro.jf.bk.account.extension.PostgresContainerExtension.Companion.injectPostgresConnectionProps
import ro.jf.bk.account.extension.UserMockIntegrationExtension
import ro.jf.bk.account.extension.UserMockIntegrationExtension.Companion.injectUserIntegrationProps
import ro.jf.bk.account.extension.givenExistingUser
import ro.jf.bk.account.extension.userIdHeader
import java.util.*

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(
    PostgresContainerExtension::class,
    UserMockIntegrationExtension::class
)
class ImportApiTest {
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

    @Test
    fun `should import transactions`(mockServerClient: MockServerClient) {
        val userId = UUID.randomUUID()
        mockServerClient.givenExistingUser(userId)
        val mockMultipartFile = ClassPathResource("mock/import.csv")
            .let { MockMultipartFile("file", it.filename, "text/csv", it.inputStream.readBytes()) }
        mockMvc.perform(
            multipart(HttpMethod.PUT, "/account/v1/import/wallet/csv")
                .file(mockMultipartFile)
                .userIdHeader(userId)
        )
            .andExpect(status().isOk)
    }
}