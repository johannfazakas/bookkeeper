package ro.jf.bk.user.web.transfer

import ro.jf.bk.user.domain.model.CreateUserCommand

data class CreateUserTO(
    val username: String,
) {
    fun toCommand() = CreateUserCommand(username = username)
}
