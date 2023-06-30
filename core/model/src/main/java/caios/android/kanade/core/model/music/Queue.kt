package caios.android.kanade.core.model.music

data class Queue(
    val items: List<Song>,
    val index: Int,
)

data class QueueItem(
    val song: Song,
    val index: Int,
)

data class LastQueue(
    val originalItems: List<Long>,
    val currentItems: List<Long>,
    val index: Int,
    val progress: Long,
)
