package ro.jf.bk.account.integration.user

import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import ro.jf.bk.account.domain.service.UserService
import java.util.*


@Component
class UserServiceAdapter(
    private val client: OkHttpClient,
    @Value("\${bookkeeper.integration.user-service.url}") private val userServiceUrl: String
) : UserService {
    override fun userExistsById(userId: UUID): Boolean {
        val request = Request.Builder()
            .url("$userServiceUrl/user/v1/users/$userId")
            .get()
            .build()
        return client.newCall(request)
            .execute()
            .use { it.isSuccessful }
    }
}
