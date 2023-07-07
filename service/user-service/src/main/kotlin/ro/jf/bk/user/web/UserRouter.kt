package ro.jf.bk.user.web

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter
import ro.jf.bk.user.web.handler.UserHandler

@Configuration
class UserRouter {
    @Bean
    fun router(
        userHandler: UserHandler
    ) = coRouter {
        accept(MediaType.APPLICATION_JSON).nest {
            "/user/v1/users".nest {
                GET("", userHandler::list)
                GET("/{userId}", userHandler::findById)
                GET("/username/{username}", userHandler::findByUsername)
                POST("", userHandler::create)
                DELETE("/{userId}", userHandler::deleteById)
            }
        }
    }
}
