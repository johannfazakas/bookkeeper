package ro.jf.bk.user.domain.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import ro.jf.bk.user.domain.UserExistsException
import ro.jf.bk.user.domain.model.CreateUserCommand
import ro.jf.bk.user.domain.model.User
import java.util.*

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun list(): Flow<User> {
        return userRepository.findAll()
    }

    suspend fun findById(userId: UUID): User? {
        return userRepository.findById(userId)
    }

    suspend fun findByUsername(username: String): User? {
        return userRepository.findByUsername(username)
    }

    suspend fun create(command: CreateUserCommand): User {
        userRepository.findByUsername(command.username)?.let { throw UserExistsException(command.username) }
        return userRepository.save(command)
    }

    suspend fun removeById(userId: UUID) {
        userRepository.deleteById(userId)
    }
}
