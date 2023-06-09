package ro.jf.stuff.account.persistence.repository

import org.springframework.data.repository.CrudRepository
import ro.jf.stuff.account.persistence.entity.Account
import java.util.*

interface AccountRepository : CrudRepository<Account, UUID>
