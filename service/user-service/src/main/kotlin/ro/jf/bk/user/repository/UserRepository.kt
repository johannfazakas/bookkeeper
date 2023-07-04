package ro.jf.bk.user.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import ro.jf.bk.user.model.User
import java.util.*

interface UserRepository : CoroutineCrudRepository<User, UUID> {
    suspend fun findByUsername(username: String): User?
    suspend fun deleteByUsername(username: String)
}
