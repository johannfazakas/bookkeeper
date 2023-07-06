package ro.jf.bk.user.handler

import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.*
import ro.jf.bk.user.repository.UserRepository
import ro.jf.bk.user.transfer.CreateUserTO
import ro.jf.bk.user.transfer.ListTO.Companion.toListTO
import ro.jf.bk.user.transfer.UserTO.Companion.toTO
import java.net.URI

@Component
class UserHandler(
    private val userRepository: UserRepository
) {
    suspend fun getUsers(request: ServerRequest): ServerResponse {
        val users = userRepository.findAll()
            .map { it.toTO() }
            .toListTO()
        return ServerResponse.ok().bodyValueAndAwait(users)
    }

    suspend fun getUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        val user = userRepository.findByUsername(username)
            ?: return ServerResponse.notFound().buildAndAwait()
        return ServerResponse.ok().bodyValueAndAwait(user.toTO())
    }

    suspend fun deleteUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        userRepository.deleteByUsername(username)
        return ServerResponse.noContent().buildAndAwait()
    }

    suspend fun createUser(request: ServerRequest): ServerResponse {
        val createUserRequest = request.awaitBody<CreateUserTO>()
        if (userRepository.findByUsername(createUserRequest.username) != null)
            return ServerResponse.unprocessableEntity().buildAndAwait()
        val user = userRepository.save(createUserRequest.toModel())
        return ServerResponse.created(URI.create("/user/v1/users/${user.username}")).bodyValueAndAwait(user.toTO())
    }
}