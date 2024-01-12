import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun App() {
    MaterialTheme {
        val accounts = listOf(
            Account("1", "Cash", "RON"),
            Account("2", "Revolut", "RON"),
            Account("3", "BT", "RON"),
        )

        Column {
            accounts.forEach { account ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    elevation = 4.dp,
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(
                            text = account.name,
                            style = MaterialTheme.typography.h5
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Currency: ${account.currency}",
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
        }
    }
}