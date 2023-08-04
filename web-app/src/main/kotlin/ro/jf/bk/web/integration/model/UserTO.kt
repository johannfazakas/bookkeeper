package ro.jf.bk.web.integration.model

import kotlinx.serialization.Serializable

@Serializable
data class UserTO(
    val id: String,
    val username: String,
)
