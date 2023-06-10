package caios.android.kanade.core.model.music

import androidx.media3.common.MediaItem

data class Queue(
    val items: List<MediaItem>,
    val index: Int,
)

data class LastQueue(
    val originalItems: List<Long>,
    val currentItems: List<Long>,
    val index: Int,
    val progress: Long,
)