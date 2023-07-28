package ro.jf.bk.apigw.web

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ro.jf.bk.apigw.integration.ProxyService

fun Application.configureRouting(
    userProxyService: ProxyService,
    accountProxyService: ProxyService
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
