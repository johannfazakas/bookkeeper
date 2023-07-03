package ro.jf.bk.user.handler

import jakarta.transaction.Transactional
import org.springframework.web.servlet.function.ServerRequest
import org.springframework.web.servlet.function.ServerResponse
import ro.jf.bk.user.transfer.CreateUserTO
import ro.jf.bk.user.transfer.ListTO.Companion.toListTO
import ro.jf.bk.user.transfer.UserTO.Companion.toTO
import ro.jf.bk.user.repository.UserRepository
import java.net.URI

class UserHandler(
    private val userRepository: UserRepository
) {
    fun getUsers(request: ServerRequest): ServerResponse {
        val users = userRepository.findAll().toList()
        return ServerResponse.ok().body(users.toListTO())
    }

    fun getUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        val user = userRepository.findByUsername(username)
            ?: return ServerResponse.notFound().build()
        return ServerResponse.ok().body(user.toTO())
    }

    fun deleteUser(request: ServerRequest): ServerResponse {
        val username = request.pathVariable("username")
        userRepository.findByUsername(username)
            ?.let { userRepository.delete(it) }
        return ServerResponse.noContent().build()
    }

    fun createUser(request: ServerRequest): ServerResponse {
        val createUserRequest = request.body(CreateUserTO::class.java)
        if (userRepository.findByUsername(createUserRequest.username) != null)
            return ServerResponse.unprocessableEntity().build()
        val user = userRepository.save(createUserRequest.toModel())
        return ServerResponse.created(URI.create("/user/v1/users/${user.username}")).body(user.toTO())
    }
}