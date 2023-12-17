package ro.jf.bk.account.api.web.interceptor

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class InterceptorConfig(
    private val userIdInterceptor: UserIdInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(userIdInterceptor)
            .addPathPatterns("/account/v1/**")
    }
}
