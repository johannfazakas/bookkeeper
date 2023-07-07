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
import java.util.*

@Component
class UserHandler(
    private val userService: UserService
) {
    suspend fun list(request: ServerRequest): ServerResponse {
        val users = userService.list()
            .map { it.toTO() }
            .toListTO()
        return ServerResponse.ok().bodyValueAndAwait(users)
    }

    suspend fun findById(request: ServerRequest): ServerResponse {
        val userId = request.userIdPathVariable()
        val user = userService.findById(userId)
            ?: return ServerResponse.notFound().buildAndAwait()
        return ServerResponse.ok().bodyValueAndAwait(user.toTO())
    }

    suspend fun findByUsername(request: ServerRequest): ServerResponse {
        val username = request.usernamePathVariable()
        val user = userService.findByUsername(username)
            ?: return ServerResponse.notFound().buildAndAwait()
        return ServerResponse.ok().bodyValueAndAwait(user.toTO())
    }

    suspend fun create(request: ServerRequest): ServerResponse = try {
        val createUserRequest = request.awaitBody<CreateUserTO>()
        val user = userService.create(createUserRequest.toCommand())
        ServerResponse.created(URI.create("/user/v1/users/${user.id}")).bodyValueAndAwait(user.toTO())
    } catch (e: UserExistsException) {
        ServerResponse.unprocessableEntity().buildAndAwait()
    }

    suspend fun deleteById(request: ServerRequest): ServerResponse {
        val userId = request.userIdPathVariable()
        userService.removeById(userId)
        return ServerResponse.noContent().buildAndAwait()
    }

    private fun ServerRequest.userIdPathVariable() = pathVariable("userId").let(UUID::fromString)

    private fun ServerRequest.usernamePathVariable() = pathVariable("username")
}
