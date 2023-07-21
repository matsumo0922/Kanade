package caios.android.kanade.core.repository

import android.database.Cursor
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import java.io.File

interface SongRepository {

    fun clear()

    fun get(songId: Long): Song?
    fun gets(songIds: List<Long>): List<Song>
    fun gets(): List<Song>

    suspend fun song(cursor: Cursor?): Song?
    suspend fun song(songId: Long, musicConfig: MusicConfig): Song?

    suspend fun songs(musicConfig: MusicConfig): List<Song>
    suspend fun songs(cursor: Cursor?): List<Song>

    suspend fun <T> useFile(song: Song, action: (File?) -> T): T
    suspend fun makeCursor(
        selection: String = "",
        selectionValues: List<String> = emptyList(),
        vararg musicOrders: MusicOrder = arrayOf(MusicOrder(Order.ASC, MusicOrderOption.Song.NAME)),
    ): Cursor?

    fun fetchArtwork()
    fun songsSort(songs: List<Song>, musicConfig: MusicConfig): List<Song>
}
