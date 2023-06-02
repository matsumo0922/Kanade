package caios.android.kanade.core.repository

import android.database.Cursor
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.MusicOrderOption
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.Song

interface SongRepository {

    fun song(cursor: Cursor?): Song
    fun song(songId: Long): Song

    fun songs(): List<Song>
    fun songs(cursor: Cursor): List<Song>

    fun makeCursor(
        selection: String = "",
        selectionValues: List<String> = emptyList(),
        musicOrder: MusicOrder = MusicOrder(Order.ASC, MusicOrderOption.Song.NAME),
    ): Cursor?
}