package api

import model.Account
import utils.log

object AccountClient {

    private var accounts = listOf(
        Account("1", "Cash", "RON"),
        Account("2", "Revolut", "RON"),
        Account("3", "BT RON", "RON"),
        Account("4", "BT EUR", "EUR"),
        Account("5", "Philocode", "RON"),
    )

    fun getAccounts() = accounts

    fun removeAccount(account: Account) {
        accounts = accounts.filter { it.id != account.id }
        log("Account $account removed. Remaining accounts: ${accounts}")
    }
}