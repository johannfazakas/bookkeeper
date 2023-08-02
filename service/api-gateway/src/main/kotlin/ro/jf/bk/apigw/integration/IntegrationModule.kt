package ro.jf.bk.apigw.integration

import io.ktor.client.*
import io.ktor.server.application.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

enum class Services(val value: String) {
    USER_SERVICE("user-service"),
    ACCOUNT_SERVICE("account-service")
}

fun Application.integrationModule() = module {
    single { HttpClient() }
    Services.entries.map(Services::value).forEach { name ->
        single(named(name)) { ProxyService(getServiceBaseUrl(name), get()) }
    }
}

private fun Application.getServiceBaseUrl(serviceName: String) =
    environment.config.property("bookkeeper.integration.${serviceName}.url").getString()
