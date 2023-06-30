package ro.jf.bk.user.service

import org.springframework.stereotype.Component
import ro.jf.bk.user.model.User
import ro.jf.bk.user.repository.UserRepository
import java.util.*

@Component
class UserService(
    private val userRepository: UserRepository,
) {

    fun getUsers(): List<User> {
        return listOf(User(UUID.randomUUID(), "user1"), User(UUID.randomUUID(), "user2"))
    }

    fun getUser(username: String): User? {
        return userRepository.findByUsername(username)
    }

    fun createUser(username: String): User {
        return User(UUID.randomUUID(), username)
    }

    fun deleteUser(username: String) {
    }
}
