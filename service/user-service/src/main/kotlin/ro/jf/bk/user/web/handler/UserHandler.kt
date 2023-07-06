package ro.jf.bk.user.web.handler

import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import ro.jf.bk.user.domain.UserExistsException
import ro.jf.bk.user.domain.service.UserService
import ro.jf.bk.user.web.transfer.CreateUserTO
import ro.jf.bk.user.web.transfer.ListTO.Companion.toListTO
import ro.jf.bk.user.web.transfer.UserTO.Companion.toTO
import java.net.URI

@Component
class UserHandler(
    private val userService: UserService
) {
    suspend fun getUsers(request: ServerRequest): ServerResponse {
        val users = userService.getUsers()
            .map { it.toTO() }
            .toListTO()
        return ServerResponse.ok().bodyValueAndAwait(users)
    }

    suspend fun getUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        val user = userService.getUser(username)
            ?: return ServerResponse.notFound().buildAndAwait()
        return ServerResponse.ok().bodyValueAndAwait(user.toTO())
    }

    suspend fun deleteUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        userService.deleteUser(username)
        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun createUser(request: ServerRequest): ServerResponse {
        val createUserRequest = request.awaitBody<CreateUserTO>()
        return try {
            val user = userService.createUser(createUserRequest.toCommand())
            ServerResponse.created(URI.create("/user/v1/users/${user.username}")).bodyValueAndAwait(user.toTO())
        } catch (e: UserExistsException) {
            return ServerResponse.unprocessableEntity().buildAndAwait()
        }
    }
}
