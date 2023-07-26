package apigw.bk.jf.ro

import io.ktor.server.application.*
import apigw.bk.jf.ro.web.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module() {
    configureSerialization()
    configureRouting()
}
