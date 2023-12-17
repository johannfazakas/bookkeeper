package ro.jf.bk.account.domain.service

import mu.KotlinLogging.logger
import org.springframework.stereotype.Service
import ro.jf.bk.account.api.transfer.TransactionImportTO
import java.util.*

val log = logger { }

@Service
class ImportService {
    fun import(userId: UUID, transactions: List<TransactionImportTO>) {
        log.info { "Importing for user $userId transactions $transactions." }
    }
}
