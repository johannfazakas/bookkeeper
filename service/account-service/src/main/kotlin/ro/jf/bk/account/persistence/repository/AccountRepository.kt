package ro.jf.bk.account.persistence.repository

import org.springframework.data.repository.CrudRepository
import ro.jf.bk.account.persistence.model.Account
import java.util.*

interface AccountRepository : CrudRepository<Account, UUID> {
    fun findByName(name: String): Account?
}

