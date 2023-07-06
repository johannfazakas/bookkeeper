package ro.jf.bk.user.persistence.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table(name = "app_user")
data class User(
    @Id
    var id: UUID? = null,
    var username: String,
)
