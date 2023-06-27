package ro.jf.bk.common

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("bk.persistence.postgres")
data class PostgresProperties(
    val url: String,
    val username: String,
    val password: String,
)
