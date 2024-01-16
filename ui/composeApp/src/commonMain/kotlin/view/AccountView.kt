package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import model.Account
import model.CreateAccountRequest
import viewmodel.AccountViewModel


@Composable
fun AccountView() {
    var accounts by remember { AccountViewModel.accounts }
    var openCreateAccountDialog by remember { mutableStateOf(false) }

    Row {
        AccountMenu(
            onCreateAccount = { openCreateAccountDialog = true }
        )
        AccountListView(
            accounts = accounts,
            onDelete = { AccountViewModel.removeAccount(it) }
        )
    }

    if (openCreateAccountDialog) {
        CreateAccountDialog(
            onDismiss = { openCreateAccountDialog = false },
            onCreateAccount = { AccountViewModel.addAccount(it) }
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateAccountDialog(
    onDismiss: () -> Unit,
    onCreateAccount: (CreateAccountRequest) -> Unit
) {
    var accountName by remember { mutableStateOf("") }
    var currency by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(375.dp)
                .padding(16.dp),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Create account",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(16.dp)
                )
                TextField(
                    value = accountName,
                    onValueChange = { accountName = it },
                    label = { Text("Account name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
                TextField(
                    value = currency,
                    onValueChange = { currency = it },
                    label = { Text("Currency") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            // Add the new account
                            // You can customize this logic based on your requirements
                            onCreateAccount(CreateAccountRequest(accountName, currency))
                            onDismiss()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Add")
                        Text("Add")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            onDismiss()
                        }
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Cancel")
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

@Composable
fun AccountMenu(
    onCreateAccount: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .fillMaxHeight()
    ) {
        Text(
            text = "Accounts",
            style = MaterialTheme.typography.h3,
            modifier = Modifier.padding(16.dp)
        )
        Button(
            onClick = onCreateAccount,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Add account")
        }
    }
}

@Composable
fun AccountListView(
    accounts: List<Account>,
    onDelete: (Account) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        accounts.forEach { account ->
            AccountItem(
                account = account,
                onDelete = { onDelete(account) }
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
            AccountDropDownMenu(
                onDelete = onDelete
            )
        }
    }
}

@Composable
fun AccountDropDownMenu(
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
