package ro.jf.bk.user.domain.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import ro.jf.bk.user.domain.UserExistsException
import ro.jf.bk.user.domain.model.CreateUserCommand
import ro.jf.bk.user.domain.model.User
import ro.jf.bk.user.persistence.entity.UserEntity
import ro.jf.bk.user.persistence.repository.UserEntityRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {
    suspend fun getUsers(): Flow<User> = userRepository.findAll()

    suspend fun getUser(username: String): User? = userRepository.findByUsername(username)

    suspend fun createUser(command: CreateUserCommand): User {
        userRepository.findByUsername(command.username)?.let { throw UserExistsException(command.username) }
        return userRepository.save(command)
    }

    suspend fun deleteUser(username: String) = userRepository.deleteByUsername(username)
}
