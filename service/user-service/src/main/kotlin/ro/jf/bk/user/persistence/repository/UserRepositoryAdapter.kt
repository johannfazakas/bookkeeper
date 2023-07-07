package ro.jf.bk.user.persistence.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.user.domain.model.CreateUserCommand
import ro.jf.bk.user.domain.model.User
import ro.jf.bk.user.domain.service.UserRepository
import ro.jf.bk.user.persistence.entity.UserEntity
import java.util.*

interface UserEntityRepository : CoroutineCrudRepository<UserEntity, UUID> {
    suspend fun findByUsername(username: String): UserEntity?
}

@Component
class UserRepositoryAdapter(
    private val userEntityRepository: UserEntityRepository
) : UserRepository {
    override suspend fun findAll(): Flow<User> =
        userEntityRepository.findAll().map(UserEntity::toDomain)

    override suspend fun findById(userId: UUID): User? =
        userEntityRepository.findById(userId)?.toDomain()

    override suspend fun findByUsername(username: String): User? =
        userEntityRepository.findByUsername(username)?.toDomain()

    override suspend fun save(command: CreateUserCommand): User =
        userEntityRepository.save(UserEntity(username = command.username)).toDomain()

    override suspend fun deleteById(userId: UUID) =
        userEntityRepository.deleteById(userId)
}