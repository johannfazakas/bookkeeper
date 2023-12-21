package ro.jf.bk.account.infrastructure.integration

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class IntegrationConfig {
    @Bean
    fun okHttpClient(): OkHttpClient {
        return OkHttpClient()
    }
}
