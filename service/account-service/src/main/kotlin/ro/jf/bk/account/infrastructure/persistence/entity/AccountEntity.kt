package ro.jf.bk.account.infrastructure.persistence.entity

import jakarta.persistence.*
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import java.util.*

@Entity
@Table(name = "account")
data class AccountEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: UUID? = null,
    var userId: UUID,
    var name: String,
    var type: String,
    var currency: String
) {
    fun toDomain(): Account {
        if (id == null) throw IllegalStateException("Account Entity id is null.")
        return Account(
            id = id!!,
            userId = userId,
            name = name,
            type = AccountType.fromValue(type),
            currency = currency
        )
    }
}
