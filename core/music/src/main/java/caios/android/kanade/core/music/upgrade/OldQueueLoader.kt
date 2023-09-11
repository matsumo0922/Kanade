package caios.android.kanade.core.music.upgrade

import android.content.Context
import caios.android.kanade.core.common.network.util.StringUtil.fromJsonToLongList
import caios.android.kanade.core.repository.MusicRepository

class OldQueueLoader(
    private val context: Context,
    private val musicRepository: MusicRepository,
) {
    fun hasOldItems(): Boolean {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val items = preference.all

        return items.isNotEmpty()
    }

    suspend fun upgrade() {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

        val queueIndex = preference.getInt(QUEUE_INDEX, -1)
        val queueList = preference.getString(QUEUE_LIST, "")?.fromJsonToLongList() ?: emptyList()
        val queueListOriginal = preference.getString(QUEUE_LIST_ORIGINAL, "")?.fromJsonToLongList() ?: emptyList()

        musicRepository.saveQueue(
            currentQueue = queueList.mapNotNull { musicRepository.getSong(it) },
            originalQueue = queueListOriginal.mapNotNull { musicRepository.getSong(it) },
            index = queueIndex,
        )

        context.deleteSharedPreferences(PREFERENCE_NAME)
    }

    companion object {
        const val PREFERENCE_NAME = "CAIOS-MusicQueue"
        const val QUEUE_LIST = "QueueList"
        const val QUEUE_INDEX = "QueueIndex"
        const val QUEUE_LIST_ORIGINAL = "QueueListOriginal"
    }
}
