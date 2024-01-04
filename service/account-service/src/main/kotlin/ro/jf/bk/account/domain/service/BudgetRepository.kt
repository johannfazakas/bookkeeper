package ro.jf.bk.account.domain.service

import ro.jf.bk.account.domain.command.BudgetCommand
import ro.jf.bk.account.domain.model.Budget
import java.time.YearMonth
import java.util.*

interface BudgetRepository {
    fun batchUpdate(userId: UUID, availableFrom: YearMonth, commands: List<BudgetCommand>)
    fun findAll(userId: UUID): List<Budget>
}
