package ro.jf.bk.account.web.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import ro.jf.bk.account.domain.service.UserService
import java.util.*

@Component
class UserIdInterceptor(
    private val userService: UserService
) : HandlerInterceptor {
    // just a very trustful way of authorizing requests and creating a context
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val userId = try {
            request.getHeader("BK_USER_ID")?.let(UUID::fromString)
        } catch (e: Exception) {
            null
        }
        if (userId == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "User id missing in header.")
            return false
        }
        if (!userService.userExistsById(userId)) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "User with id $userId does not exist.")
            return false
        }
        return true
    }
}
