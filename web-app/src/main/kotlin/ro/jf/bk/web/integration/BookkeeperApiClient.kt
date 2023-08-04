package ro.jf.bk.web.integration

import ro.jf.bk.web.integration.model.UsersResponse
import kotlinx.browser.window
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

suspend fun getUsers(): UsersResponse {
    val response = window.fetch("http://localhost:8101/user/v1/users")
        .await()
        .text()
        .await()
    return Json.decodeFromString(response)
}
