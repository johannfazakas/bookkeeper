package ro.jf.bk.user.extension

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.extension.AfterAllCallback
import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import kotlin.Nothing
import kotlin.apply

class PostgresContainerExtension : BeforeAllCallback, AfterAllCallback {
    companion object {
        private lateinit var container: PostgreSQLContainer<*>

        fun DynamicPropertyRegistry.injectPostgresConnectionProps() {
            add("spring.datasource.url") { container.jdbcUrl }
            add("spring.datasource.username") { container.username }
            add("spring.datasource.password") { container.password }
        }
    }

    override fun beforeAll(context: ExtensionContext?) {
        container = PostgreSQLContainer<Nothing>(DockerImageName.parse("postgres:15.2"))
            .apply {
                withDatabaseName("bookkeeper-user-db")
                withUsername("root")
                withPassword("pass")
                // add `testcontainers.reuse.enable=true` in ~/.testcontainers.properties for local container reuse
                withReuse(true)
            }
        container.start()
    }

    override fun afterAll(context: ExtensionContext?) {
        Flyway.configure()
            .dataSource(container.jdbcUrl, container.username, container.password)
            .configuration(mapOf("flyway.cleanDisabled" to false.toString()))
            .load()
            .clean()
    }
}
