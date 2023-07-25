package ro.jf.bk.account.extension

import org.junit.jupiter.api.extension.*
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName
import java.util.*

class UserMockIntegrationExtension : BeforeAllCallback, AfterAllCallback, ParameterResolver {
    companion object {
        private lateinit var mockServerContainer: MockServerContainer
        private lateinit var mockServerClient: MockServerClient

        fun DynamicPropertyRegistry.injectUserIntegrationProps() {
            add("bookkeeper.integration.user-service.url") { mockServerUrl() }
        }

        private fun mockServerUrl() = "http://${mockServerContainer.host}:${mockServerContainer.serverPort}"
    }

    override fun beforeAll(context: ExtensionContext) {
        val dockerImage = DockerImageName.parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient::class.java.getPackage().implementationVersion)
        mockServerContainer = MockServerContainer(dockerImage)
            // add `testcontainers.reuse.enable=true` in ~/.testcontainers.properties for local container reuse
            .withReuse(true)
        mockServerContainer.start()
        mockServerClient = MockServerClient(mockServerContainer.host, mockServerContainer.serverPort)
    }

    override fun afterAll(context: ExtensionContext) {
        mockServerClient.reset()
    }

    override fun supportsParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Boolean {
        return parameterContext.parameter.type == MockServerClient::class.java
    }

    override fun resolveParameter(parameterContext: ParameterContext, extensionContext: ExtensionContext): Any {
        return mockServerClient
    }
}

fun MockServerClient.givenExistingUser(userId: UUID) {
    `when`(
        request()
            .withMethod("GET")
            .withPath("/user/v1/users/$userId")
    ).respond(
        response()
            .withStatusCode(200)
            .withBody(
                """
                    {
                        "id": "$userId",
                        "username": "user"
                    }
                    """.trimIndent()
            )
    )
}

fun MockServerClient.givenNonExistingUser(userId: UUID) {
    `when`(
        request()
            .withMethod("GET")
            .withPath("/user/v1/users/$userId")
    ).respond(
        response()
            .withStatusCode(404)
    )
}
