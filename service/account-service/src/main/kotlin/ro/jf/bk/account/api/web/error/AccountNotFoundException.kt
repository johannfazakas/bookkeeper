package ro.jf.bk.account.api.web.error

import java.util.*

class AccountNotFoundException(val userId: UUID, val accountId: UUID) :
    RuntimeException("Account $accountId not found for user $userId") {
    companion object {
        const val TITLE = "Account not found."
    }
}
