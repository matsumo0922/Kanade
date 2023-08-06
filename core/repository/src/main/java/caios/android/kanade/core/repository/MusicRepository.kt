package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.AlbumDetail
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.ArtistDetail
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.PlayHistory
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

interface MusicRepository {

    val config: Flow<MusicConfig>
    val lastQueue: Flow<LastQueue>
    val updateFlag: StateFlow<Int>

    val songs: List<Song>
    val artists: List<Artist>
    val albums: List<Album>
    val playlists: List<Playlist>
    val playHistory: List<PlayHistory>

    suspend fun clear()
    suspend fun refresh()

    fun sortedSongs(musicConfig: MusicConfig): List<Song>
    fun sortedArtists(musicConfig: MusicConfig): List<Artist>
    fun sortedAlbums(musicConfig: MusicConfig): List<Album>
    fun sortedPlaylists(musicConfig: MusicConfig): List<Playlist>

    fun getSong(songId: Long): Song?
    fun getArtist(artistId: Long): Artist?
    fun getAlbum(albumId: Long): Album?
    fun getPlaylist(playlistId: Long): Playlist?
    fun getLyrics(song: Song): Lyrics?
    fun getPlayHistory(song: Song): List<PlayHistory>
    fun getArtistDetail(artist: Artist): ArtistDetail?
    fun getAlbumDetail(album: Album): AlbumDetail?

    suspend fun saveQueue(currentQueue: List<Song>, originalQueue: List<Song>, index: Int)
    suspend fun saveProgress(progress: Long)

    suspend fun fetchSongs(musicConfig: MusicConfig? = null)
    suspend fun fetchArtists(musicConfig: MusicConfig? = null)
    suspend fun fetchAlbums(musicConfig: MusicConfig? = null)
    suspend fun fetchPlaylist(musicConfig: MusicConfig? = null)
    suspend fun fetchArtistArtwork()
    suspend fun fetchAlbumArtwork()
    suspend fun fetchLyrics(song: Song)
    suspend fun fetchPlayHistory()

    suspend fun createPlaylist(name: String, songs: List<Song>)
    suspend fun removePlaylist(playlist: Playlist)
    suspend fun renamePlaylist(playlist: Playlist, name: String)
    suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>)
    suspend fun removeFromPlaylist(playlist: Playlist, index: Int)
    suspend fun moveItemInPlaylist(playlist: Playlist, fromIndex: Int, toIndex: Int)

    suspend fun isFavorite(song: Song): Boolean
    suspend fun addToFavorite(song: Song)
    suspend fun removeFromFavorite(song: Song)

    suspend fun addToPlayHistory(song: Song)
    suspend fun getPlayedCount(): Map<Song, Int>

    suspend fun setShuffleMode(mode: ShuffleMode)
    suspend fun setRepeatMode(mode: RepeatMode)

    suspend fun setSongOrder(musicOrder: MusicOrder)
    suspend fun setArtistOrder(musicOrder: MusicOrder)
    suspend fun setAlbumOrder(musicOrder: MusicOrder)
    suspend fun setPlaylistOrder(musicOrder: MusicOrder)

    suspend fun <T> useSongFile(song: Song, action: (File?) -> T): T
}
