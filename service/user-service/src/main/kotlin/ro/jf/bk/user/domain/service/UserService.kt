package ro.jf.bk.user.domain.service

import org.springframework.stereotype.Component
import ro.jf.bk.user.domain.model.User
import java.util.*

@Component
class UserService {

    fun getUsers(): List<User> {
        return listOf(User(UUID.randomUUID(), "user1"), User(UUID.randomUUID(), "user2"));
    }

    fun getUser(username: String): User {
        return User(UUID.randomUUID(), username);
    }

    fun createUser(username: String): User {
        return User(UUID.randomUUID(), username);
    }

    fun deleteUser(username: String) {
    }
}
