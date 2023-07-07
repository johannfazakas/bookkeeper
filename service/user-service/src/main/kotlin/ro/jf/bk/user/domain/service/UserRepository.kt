package ro.jf.bk.user.domain.service

import kotlinx.coroutines.flow.Flow
import ro.jf.bk.user.domain.model.CreateUserCommand
import ro.jf.bk.user.domain.model.User
import java.util.*

interface UserRepository {
    suspend fun findAll(): Flow<User>
    suspend fun findById(userId: UUID): User?
    suspend fun findByUsername(username: String): User?
    suspend fun save(command: CreateUserCommand): User
    suspend fun deleteById(userId: UUID)
}
