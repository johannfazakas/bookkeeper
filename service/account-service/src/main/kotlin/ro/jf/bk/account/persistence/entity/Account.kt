package ro.jf.bk.account.persistence.entity

import java.util.*
import jakarta.persistence.*

@Entity
@Table(name = "account")
data class Account(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var name: String,
    var currency: String
)
