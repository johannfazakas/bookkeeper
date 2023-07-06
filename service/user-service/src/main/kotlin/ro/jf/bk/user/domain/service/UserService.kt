package ro.jf.bk.user.domain.service

import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import ro.jf.bk.user.persistence.model.User
import ro.jf.bk.user.persistence.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository
) {
    suspend fun getUsers(): Flow<User> = userRepository.findAll()

    suspend fun getUser(username: String): User? = userRepository.findByUsername(username)

    suspend fun createUser(username: String) = userRepository.save(User(username = username))

    suspend fun deleteUser(username: String) = userRepository.deleteByUsername(username)
}
