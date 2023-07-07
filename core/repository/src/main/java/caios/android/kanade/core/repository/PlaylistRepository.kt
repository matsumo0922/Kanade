package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import kotlinx.coroutines.flow.SharedFlow

interface PlaylistRepository {

    val data: SharedFlow<List<Playlist>>

    fun get(playlistId: Long): Playlist?
    fun gets(): List<Playlist>

    suspend fun playlist(playlistId: Long): Playlist?
    suspend fun playlists(musicConfig: MusicConfig): List<Playlist>

    suspend fun create(playlist: Playlist)
    suspend fun remove(playlist: Playlist)
    suspend fun rename(playlist: Playlist, name: String)

    suspend fun addItems(playlistId: Long, songs: List<Song>)
    suspend fun removeItem(playlistId: Long, index: Int)
    suspend fun moveItem(playlistId: Long, fromIndex: Int, toIndex: Int)

    suspend fun isFavorite(song: Song): Boolean
    suspend fun addToFavorite(song: Song)
    suspend fun removeFromFavorite(song: Song)

    fun playlistSort(playlists: List<Playlist>, musicConfig: MusicConfig): List<Playlist>
}
