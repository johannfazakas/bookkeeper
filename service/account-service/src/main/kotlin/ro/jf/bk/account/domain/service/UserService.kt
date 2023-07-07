package ro.jf.bk.account.domain.service

import java.util.*

interface UserService {
    fun userExistsById(userId: UUID): Boolean
}
