package ro.jf.bk.user.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ro.jf.bk.user.model.User
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(username: String): User?
    fun deleteByUsername(username: String)
}
