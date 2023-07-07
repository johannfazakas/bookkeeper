package ro.jf.bk.account.persistence.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import ro.jf.bk.account.domain.service.AccountRepository
import ro.jf.bk.account.persistence.entity.AccountEntity
import java.util.*

interface AccountEntityRepository : CrudRepository<AccountEntity, UUID> {
    fun findByUserId(userId: UUID): List<AccountEntity>
    fun findByUserIdAndName(userId: UUID, name: String): AccountEntity?
}

@Component
class AccountRepositoryAdapter(
    private val accountEntityRepository: AccountEntityRepository
) : AccountRepository {
    override fun findAll(userId: UUID): List<Account> =
        accountEntityRepository.findByUserId(userId).map(AccountEntity::toDomain)

    override fun save(command: CreateAccountCommand): Account =
        accountEntityRepository.save(
            AccountEntity(
                userId = command.userId,
                name = command.name,
                currency = command.currency
            )
        ).toDomain()
}
