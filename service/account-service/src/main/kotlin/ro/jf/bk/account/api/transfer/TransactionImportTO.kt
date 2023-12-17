package ro.jf.bk.account.api.transfer

import java.math.BigDecimal

data class TransactionImportTO(
    val accountName: String,
    val category: String,
    val amount: BigDecimal,
    val currency: String,
    val metadata: Map<String, Any>,
)
