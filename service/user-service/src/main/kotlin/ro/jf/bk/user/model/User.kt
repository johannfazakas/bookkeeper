package ro.jf.bk.user.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "app_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var username: String,
)
