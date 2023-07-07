package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.PlayHistory
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.SharedFlow

interface PlayHistoryRepository {

    val data: SharedFlow<List<PlayHistory>>

    fun gets(song: Song): List<PlayHistory>
    fun gets(): List<PlayHistory>

    suspend fun playHistory(song: Song): List<PlayHistory>
    suspend fun playHistories(): List<PlayHistory>

    fun add(song: Song)
}
