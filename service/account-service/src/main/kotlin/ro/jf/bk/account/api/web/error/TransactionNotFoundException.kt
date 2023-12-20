package ro.jf.bk.account.api.web.error

import java.util.*

class TransactionNotFoundException(val userId: UUID, val transactionId: UUID) :
    RuntimeException("Transaction with id $transactionId not found for user $userId.") {
    companion object {
        const val TITLE = "Transaction not found."
    }
}
