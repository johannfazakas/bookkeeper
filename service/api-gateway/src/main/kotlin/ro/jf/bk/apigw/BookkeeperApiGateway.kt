package ro.jf.bk.apigw

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.core.qualifier.named
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ro.jf.bk.apigw.integration.*
import ro.jf.bk.apigw.web.configureRouting

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
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
