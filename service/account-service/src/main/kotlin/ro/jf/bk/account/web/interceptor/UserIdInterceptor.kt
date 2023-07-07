package ro.jf.bk.account.web.interceptor

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor

@Component
class UserIdInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val headerValue = request.getHeader("BK_USER_ID")

        if (headerValue == null) {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "User id missing in header.")
            return false
        }
        return true
    }
}
