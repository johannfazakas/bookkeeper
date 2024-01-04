package ro.jf.bk.account.infrastructure.persistence.repository

import org.springframework.stereotype.Repository
import ro.jf.bk.account.domain.command.BudgetCommand
import ro.jf.bk.account.domain.model.Budget
import ro.jf.bk.account.domain.service.BudgetRepository
import java.time.YearMonth
import java.util.*

@Repository
class BudgetRepositoryAdapter : BudgetRepository {
    override fun batchUpdate(userId: UUID, availableFrom: YearMonth, commands: List<BudgetCommand>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userId: UUID): List<Budget> {
        TODO("Not yet implemented")
    }
}
