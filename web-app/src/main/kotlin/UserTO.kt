import kotlinx.serialization.Serializable

@Serializable
data class UserTO(
    val id: String,
    val username: String,
)
