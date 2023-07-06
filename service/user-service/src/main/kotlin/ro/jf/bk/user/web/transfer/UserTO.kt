package ro.jf.bk.user.web.transfer

import ro.jf.bk.user.persistence.model.User
import java.util.*

data class UserTO(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun User.toTO(): UserTO = UserTO(
            id = id ?: throw IllegalStateException("User id is null"),
            username = username
        )
    }
}
