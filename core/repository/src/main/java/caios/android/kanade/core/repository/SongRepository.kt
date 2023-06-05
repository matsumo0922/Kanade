package caios.android.kanade.core.repository

import android.database.Cursor
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.MusicOrderOption
import caios.android.kanade.core.model.music.Song

interface SongRepository {

    suspend fun song(cursor: Cursor?): Song?
    suspend fun song(songId: Long, musicConfig: MusicConfig): Song?

    suspend fun songs(musicConfig: MusicConfig): List<Song>
    suspend fun songs(cursor: Cursor?): List<Song>

    fun makeCursor(
        selection: String = "",
        selectionValues: List<String> = emptyList(),
        vararg musicOrders: MusicOrder = arrayOf(MusicOrder(Order.ASC, MusicOrderOption.Song.NAME)),
    ): Cursor?
}
