import kotlinx.serialization.Serializable

@Serializable
data class MindFragmentJson(
    val title: String,
    val blocks: List<BlockJson>
)

@Serializable
data class BlockJson(
    val id: String,
    val text: String
)
