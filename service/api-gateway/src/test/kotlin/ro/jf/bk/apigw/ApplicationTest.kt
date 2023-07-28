package ro.jf.bk.apigw

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.testing.*
import kotlin.test.*
import io.ktor.http.*
import ro.jf.bk.apigw.web.configureRouting

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
//            configureRouting(null, null)
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}
