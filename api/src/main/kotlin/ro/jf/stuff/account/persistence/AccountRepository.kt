package ro.jf.stuff.account.persistence

import org.springframework.data.repository.CrudRepository
import ro.jf.stuff.account.entity.Account
import java.util.*

interface AccountRepository : CrudRepository<Account, UUID>
