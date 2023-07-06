package ro.jf.bk.user.web.transfer

import ro.jf.bk.user.persistence.model.User

data class CreateUserTO(
    val username: String,
) {
    fun toModel() = User(username = username)
}
