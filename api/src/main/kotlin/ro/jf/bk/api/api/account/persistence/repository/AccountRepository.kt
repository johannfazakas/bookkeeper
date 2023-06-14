package ro.jf.bk.api.api.account.persistence.repository

import org.springframework.data.repository.CrudRepository
import ro.jf.bk.api.api.account.persistence.entity.Account
import java.util.*

interface AccountRepository : CrudRepository<Account, UUID>
