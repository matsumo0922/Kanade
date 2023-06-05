package caios.android.kanade.core.model.music

import androidx.media3.common.MediaItem

data class Queue(
    val currentQueueItems: List<MediaItem>,
    val originalQueueItems: List<MediaItem>,
    val index: Int,
)