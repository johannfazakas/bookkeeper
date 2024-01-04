package ro.jf.bk.account.api.transfer

import com.fasterxml.jackson.annotation.JsonFormat
import ro.jf.bk.account.domain.command.CreateTransactionCommand
import java.math.BigDecimal
import java.time.Instant
import java.util.*

data class CreateTransactionTO(
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    val timestamp: Instant,
    val from: UUID,
    val to: UUID,
    val amount: BigDecimal,
    val description: String?
) {
    fun toCommand(): CreateTransactionCommand = CreateTransactionCommand(
        timestamp = timestamp,
        from = from,
        to = to,
        amount = amount,
        description = description
    )
}
