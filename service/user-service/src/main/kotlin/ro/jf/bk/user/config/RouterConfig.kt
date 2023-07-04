package ro.jf.bk.user.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter
import ro.jf.bk.user.handler.UserHandler

@Configuration
class RouterConfig {
    @Bean
    fun router(
        userHandler: UserHandler
    ) = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            "/user/v1/users".nest {
                GET("", userHandler::getUsers)
                GET("/{username}", userHandler::getUser)
                POST("", userHandler::createUser)
                DELETE("/{username}", userHandler::deleteUser)
            }
        }
    }
}
