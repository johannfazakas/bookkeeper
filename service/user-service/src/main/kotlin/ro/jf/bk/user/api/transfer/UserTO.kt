package ro.jf.bk.user.api.transfer

import ro.jf.bk.user.model.User
import java.util.*

data class UserTO(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun fromDomain(user: User): UserTO = UserTO(
            id = user.id!!,
            username = user.username
        )
    }
}
