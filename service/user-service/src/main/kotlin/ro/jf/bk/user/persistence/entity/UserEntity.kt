package ro.jf.bk.user.persistence.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import ro.jf.bk.user.domain.model.User
import java.util.*

@Table(name = "app_user")
data class UserEntity(
    @Id
    var id: UUID? = null,
    var username: String,
) {
    fun toDomain(): User {
        if (id == null) throw IllegalStateException("User Entity id is null.")
        return User(
            id = id!!,
            username = username
        )
    }
}
