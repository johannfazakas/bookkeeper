package ro.jf.bk.user.config

import org.flywaydb.core.Flyway
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import ro.jf.bk.common.PostgresProperties
import javax.sql.DataSource

private const val USER_SCHEMA = "user"
private const val USER_REPOSITORY_PACKAGE = "ro.jf.bk.user.persistence.repository"
private const val USER_ENTITY_PACKAGE = "ro.jf.bk.user.persistence.entity"
private const val USER_FLYWAY_MIGRATION_PACKAGE = "classpath:db/migration/user"

@Configuration
@EnableJpaRepositories(
    basePackages = [USER_REPOSITORY_PACKAGE],
    entityManagerFactoryRef = "userEntityManagerFactory",
    transactionManagerRef = "userTransactionManager"
)
class UserPersistenceConfig(
    private val postgresProperties: PostgresProperties
) {
    @Bean
    fun userDataSource(): DataSource =
        PGSimpleDataSource().apply {
            setURL(postgresProperties.url)
            user = postgresProperties.username
            password = postgresProperties.password
            currentSchema = USER_SCHEMA
        }

    @Bean
    fun userEntityManagerFactory(
        @Qualifier("userDataSource") dataSource: DataSource,
        jpaVendorAdapter: JpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan(USER_ENTITY_PACKAGE)
            setJpaVendorAdapter(jpaVendorAdapter)
        }

    @Bean
    fun userTransactionManager(
        @Qualifier("userEntityManagerFactory") entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager =
        JpaTransactionManager(entityManagerFactory.getObject()!!)

    @Bean
    fun userFlywayInitializer(@Qualifier("userFlyway") flyway: Flyway) = FlywayMigrationInitializer(flyway)

    @Bean
    fun userFlyway(@Qualifier("userDataSource") dataSource: DataSource) =
        Flyway.configure()
            .locations(USER_FLYWAY_MIGRATION_PACKAGE)
            .dataSource(dataSource)
            .load()
}
