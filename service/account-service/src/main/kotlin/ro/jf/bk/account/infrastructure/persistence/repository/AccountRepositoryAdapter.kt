package ro.jf.bk.account.infrastructure.persistence.repository

import jakarta.transaction.Transactional
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.model.Account
import ro.jf.bk.account.domain.model.AccountType
import ro.jf.bk.account.domain.model.CreateAccountCommand
import ro.jf.bk.account.domain.service.AccountRepository
import ro.jf.bk.account.infrastructure.persistence.entity.AccountEntity
import java.util.*

interface AccountEntityRepository : CrudRepository<AccountEntity, UUID> {
    fun findByUserIdAndTypeAndId(userId: UUID, accountType: String, accountId: UUID): AccountEntity?
    fun findByUserIdAndType(userId: UUID, accountType: String): List<AccountEntity>
    fun findByUserId(userId: UUID): List<AccountEntity>
    fun findByUserIdAndTypeAndName(userId: UUID, accountType: String, name: String): AccountEntity?
    fun deleteByUserIdAndTypeAndId(userId: UUID, accountType: String, accountId: UUID)
}

@Component
@Transactional
class AccountRepositoryAdapter(
    private val accountEntityRepository: AccountEntityRepository
) : AccountRepository {
    override fun find(userId: UUID, accountType: AccountType, accountId: UUID): Account? =
        accountEntityRepository.findByUserIdAndTypeAndId(userId, accountType.value, accountId)?.toDomain()

    override fun findAllByType(userId: UUID, accountType: AccountType): List<Account> =
        accountEntityRepository.findByUserIdAndType(userId, accountType.value).map(AccountEntity::toDomain)

    override fun findAll(userId: UUID): List<Account> =
        accountEntityRepository.findByUserId(userId).map(AccountEntity::toDomain)

    override fun save(userId: UUID, command: CreateAccountCommand): Account =
        accountEntityRepository.save(
            AccountEntity(
                userId = userId,
                name = command.name,
                externalReference = command.externalReference,
                type = command.type.value,
                currency = command.currency
            )
        ).toDomain()

    override fun delete(userId: UUID, accountType: AccountType, accountId: UUID) {
        accountEntityRepository.deleteByUserIdAndTypeAndId(userId, accountType.value, accountId)
    }
}
