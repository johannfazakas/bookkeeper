package ro.jf.bk.apigw.integration

import io.ktor.client.*
import io.ktor.server.application.*
import org.koin.dsl.module

fun Application.integrationModule() = module {
    single { HttpClient() }
    single { UserProxyService(getServiceBaseUrl("user-service"), get()) }
    single { AccountProxyService(getServiceBaseUrl("account-service"), get()) }
}

private fun Application.getServiceBaseUrl(serviceName: String) =
    environment.config.property("bookkeeper.integration.${serviceName}.baseUrl").getString()
