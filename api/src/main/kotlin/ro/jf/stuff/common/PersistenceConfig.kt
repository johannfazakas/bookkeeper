package ro.jf.stuff.common

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.orm.jpa.JpaVendorAdapter
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter

private const val HIBERNATE_POSTGRES_DIALECT = "org.hibernate.dialect.PostgreSQLDialect"

@Configuration
class PersistenceConfig {
    @Bean
    fun jpaVendorAdapter(): JpaVendorAdapter {
        val adapter = HibernateJpaVendorAdapter()
        adapter.setShowSql(true)
        adapter.setDatabasePlatform(HIBERNATE_POSTGRES_DIALECT)
        return adapter
    }
}
