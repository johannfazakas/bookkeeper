package ro.jf.bk.account.web.transfer

import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.persistence.entity.AccountEntity
import java.util.*

data class AccountTO(
    val id: UUID,
    val name: String,
    val currency: String
) {
    companion object {
        fun Account.toTO(): AccountTO = AccountTO(
            id = this.id,
            name = this.name,
            currency = this.currency
        )
    }
}
