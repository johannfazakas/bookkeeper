package ro.jf.bk.apigw

import ro.jf.bk.apigw.integration.AccountProxyService
import ro.jf.bk.apigw.integration.UserProxyService
import ro.jf.bk.apigw.integration.integrationModule
import ro.jf.bk.apigw.web.configureRouting
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        slf4jLogger()
        modules(integrationModule())
    }

    val userProxyService: UserProxyService by inject()
    val accountProxyService: AccountProxyService by inject()

    configureRouting(
        userProxyService,
        accountProxyService
    )
}
