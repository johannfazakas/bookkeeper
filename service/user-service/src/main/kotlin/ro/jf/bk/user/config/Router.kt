package ro.jf.bk.user.config

import org.springframework.web.servlet.function.router
import ro.jf.bk.user.handler.UserHandler

fun userRouter(
    userHandler: UserHandler
) = router {
    "/user/v1/users".nest {
        GET("", userHandler::getUsers)
        GET("/{username}", userHandler::getUser)
        POST("", userHandler::createUser)
        DELETE("/{username}", userHandler::deleteUser)
    }
}
