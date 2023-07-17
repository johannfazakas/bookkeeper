package ro.jf.bk.account.persistence.repository

import jakarta.transaction.Transactional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.CreateAccountCommand
import ro.jf.bk.account.domain.service.AccountRepository
import ro.jf.bk.account.persistence.entity.AccountEntity
import java.util.*

interface AccountEntityRepository : CrudRepository<AccountEntity, UUID> {
    fun findByUserIdAndId(userId: UUID, accountId: UUID): AccountEntity?
    fun findByUserId(userId: UUID): List<AccountEntity>
    fun findByUserIdAndName(userId: UUID, name: String): AccountEntity?

    fun deleteByUserIdAndId(userId: UUID, accountId: UUID)
}

@Component
@Transactional
class AccountRepositoryAdapter(
    private val accountEntityRepository: AccountEntityRepository
) : AccountRepository {
    override fun find(userId: UUID, accountId: UUID): Account? =
        accountEntityRepository.findByUserIdAndId(userId, accountId)?.toDomain()

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

    override fun delete(userId: UUID, accountId: UUID) {
        accountEntityRepository.deleteByUserIdAndId(userId, accountId)
    }
}
