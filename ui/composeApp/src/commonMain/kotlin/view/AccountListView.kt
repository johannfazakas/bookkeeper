package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import api.AccountClient
import model.Account

@Composable
fun AccountListView() {
    var accounts by remember { mutableStateOf(AccountClient.getAccounts()) }

    fun removeAccount(account: Account) {
        AccountClient.removeAccount(account)
        accounts = AccountClient.getAccounts()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        accounts.forEach { account ->
            AccountItem(
                account = account,
                onDelete = { removeAccount(account) }
            )
        }
    }
}

@Composable
fun AccountItem(
    account: Account,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.h5,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = "Currency: ${account.currency}",
                style = MaterialTheme.typography.body1,
                modifier = Modifier.weight(1f)
            )
            AccountMenu(
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun AccountMenu(
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.padding(8.dp)
        ) {
            DropdownMenuItem(onClick = {
                expanded = false
                onDelete()
            }) {
                Text(text = "Delete")
            }
        }
    }
}
