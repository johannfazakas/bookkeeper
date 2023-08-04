package ro.jf.bk.web.integration.model

import kotlinx.serialization.Serializable

@Serializable
data class UsersResponse(
    val data: List<UserTO>
) {
    companion object {
        fun empty(): UsersResponse = UsersResponse(data = emptyList())
    }
}
