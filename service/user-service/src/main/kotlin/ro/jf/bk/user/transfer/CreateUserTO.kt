package ro.jf.bk.user.transfer

import ro.jf.bk.user.model.User

data class CreateUserTO(
    val username: String,
) {
    fun toModel() = User(username = username)
}
