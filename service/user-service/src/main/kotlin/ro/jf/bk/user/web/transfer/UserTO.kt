package ro.jf.bk.user.web.transfer

import ro.jf.bk.user.domain.model.User
import java.util.*

data class UserTO(
    val id: UUID,
    val username: String,
) {
    companion object {
        fun User.toTO(): UserTO = UserTO(
            id = id,
            username = username
        )
    }
}
