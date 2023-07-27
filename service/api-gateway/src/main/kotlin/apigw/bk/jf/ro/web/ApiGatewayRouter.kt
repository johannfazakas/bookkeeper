package apigw.bk.jf.ro.web

import apigw.bk.jf.ro.integration.AccountProxyService
import apigw.bk.jf.ro.integration.UserProxyService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    userProxyService: UserProxyService,
    accountProxyService: AccountProxyService
) {
    routing {
        route("/user/v1/*") {
            handle {
                userProxyService.proxy(call)
            }
        }
        route("/account/v1/*") {
            handle {
                accountProxyService.proxy(call)
            }
        }
    }
}
