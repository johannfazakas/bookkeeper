package ro.jf.bk.apigw.integration

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

private const val USER_ID_HEADER = "BK_USER_ID"

sealed class ProxyService(
    private val baseUrl: String,
    private val httpClient: HttpClient
) {
    suspend fun proxy(call: ApplicationCall) {
        val url = "${baseUrl}${call.request.path()}"
        val proxyResponse: HttpResponse = httpClient.request(url) {
            method = call.request.httpMethod
            headers {
                call.request.headers[USER_ID_HEADER]?.let { set(USER_ID_HEADER, it) }
                call.request.headers[HttpHeaders.ContentType]?.let { set(HttpHeaders.ContentType, it) }
            }
            setBody(call.receive<ByteArray>())
        }
        call.respondBytes(
            contentType = proxyResponse.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }
                ?: ContentType.Application.Json,
            status = proxyResponse.status
        ) { proxyResponse.body() }
    }
}

class AccountProxyService(
    baseUrl: String,
    httpClient: HttpClient
) : ProxyService(
    baseUrl,
    httpClient
)

class UserProxyService(
    baseUrl: String,
    httpClient: HttpClient
) : ProxyService(
    baseUrl,
    httpClient
)
