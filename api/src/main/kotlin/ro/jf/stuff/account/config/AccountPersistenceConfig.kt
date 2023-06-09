package ro.jf.stuff.account.config

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
import ro.jf.stuff.common.PostgresProperties
import javax.sql.DataSource

private const val ACCOUNT_SCHEMA = "account"
private const val ACCOUNT_REPOSITORY_PACKAGE = "ro.jf.stuff.account.persistence.repository"
private const val ACCOUNT_ENTITY_PACKAGE = "ro.jf.stuff.account.persistence.entity"
private const val ACCOUNT_FLYWAY_MIGRATION_PACKAGE = "classpath:db/migration/account"

@Configuration
@EnableJpaRepositories(
    basePackages = [ACCOUNT_REPOSITORY_PACKAGE],
    entityManagerFactoryRef = "accountEntityManagerFactory",
    transactionManagerRef = "accountTransactionManager"
)
class AccountPersistenceConfig(
    private val postgresProperties: PostgresProperties
) {
    @Bean
    fun accountDataSource(): DataSource =
        PGSimpleDataSource().apply {
            setURL(postgresProperties.url)
            user = postgresProperties.username
            password = postgresProperties.password
            currentSchema = ACCOUNT_SCHEMA
        }

    @Bean
    fun accountEntityManagerFactory(
        @Qualifier("accountDataSource") dataSource: DataSource,
        jpaVendorAdapter: JpaVendorAdapter
    ): LocalContainerEntityManagerFactoryBean =
        LocalContainerEntityManagerFactoryBean().apply {
            setDataSource(dataSource)
            setPackagesToScan(ACCOUNT_ENTITY_PACKAGE)
            setJpaVendorAdapter(jpaVendorAdapter)
        }

    @Bean
    fun accountTransactionManager(
        @Qualifier("accountEntityManagerFactory") entityManagerFactory: LocalContainerEntityManagerFactoryBean
    ): PlatformTransactionManager =
        JpaTransactionManager(entityManagerFactory.getObject()!!)

    @Bean
    fun accountFlywayInitializer(@Qualifier("accountFlyway") flyway: Flyway) = FlywayMigrationInitializer(flyway)

    @Bean
    fun accountFlyway(@Qualifier("accountDataSource") dataSource: DataSource?) =
        Flyway.configure()
            .locations(ACCOUNT_FLYWAY_MIGRATION_PACKAGE)
            .dataSource(dataSource)
            .load()
}
