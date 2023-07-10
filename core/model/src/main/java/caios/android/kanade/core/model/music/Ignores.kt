import kotlinx.serialization.Serializable

@Serializable
data class Ignores(
    val artistIds: List<Long>,
    val albumIds: List<Long>,
)
