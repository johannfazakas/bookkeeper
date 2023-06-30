package ro.jf.bk.user.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import ro.jf.bk.user.exception.UserAlreadyExistsException
import ro.jf.bk.user.exception.UserNotFoundException
import ro.jf.bk.user.model.User
import ro.jf.bk.user.repository.UserRepository

@Component
@Transactional
class UserService(
    private val userRepository: UserRepository,
) {

    fun getUsers(): List<User> {
        return userRepository.findAll().toList()
    }

    fun getUser(username: String): User {
        return userRepository.findByUsername(username)
            ?: throw UserNotFoundException(username)
    }

    fun createUser(username: String): User {
        userRepository.findByUsername(username)?.let { throw UserAlreadyExistsException(username) }
        return userRepository.save(User(username = username))
    }

    fun deleteUser(username: String) {
        userRepository.deleteByUsername(username)
    }
}
