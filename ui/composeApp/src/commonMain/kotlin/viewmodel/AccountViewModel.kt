package viewmodel

import androidx.compose.runtime.mutableStateOf
import api.AccountClient
import model.Account
import model.CreateAccountRequest

object AccountViewModel {
    val accounts = mutableStateOf<List<Account>>(emptyList())

    init {
        refreshAccounts()
    }

    fun removeAccount(account: Account) {
        AccountClient.removeAccount(account)
        refreshAccounts()
    }

    fun addAccount(request: CreateAccountRequest) {
        AccountClient.createAccount(request)
        refreshAccounts()
    }

    private fun refreshAccounts() {
        accounts.value = AccountClient.getAccounts()
    }
}
