package ro.jf.bk.apigw

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ro.jf.bk.apigw.integration.ProxyService
import ro.jf.bk.apigw.integration.Services
import ro.jf.bk.apigw.integration.integrationModule
import ro.jf.bk.apigw.web.configureRouting

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(CORS) {
        anyHost()
    }
    install(Koin) {
        slf4jLogger()
        modules(integrationModule())
    }

    val userProxyService: ProxyService by inject(named(Services.USER_SERVICE.value))
    val accountProxyService: ProxyService by inject(named(Services.ACCOUNT_SERVICE.value))

    configureRouting(
        userProxyService,
        accountProxyService
    )
}
