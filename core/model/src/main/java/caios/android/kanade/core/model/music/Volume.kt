package caios.android.kanade.core.model.music

import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    val songId: Long,
    val fileSize: Long,
    val encoder: String?,
    val meanVolume: Double?,
    val maxVolume: Double?,
)
