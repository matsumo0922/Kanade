package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    val config: Flow<MusicConfig>
    val lastQueue: Flow<LastQueue>

    val songs: List<Song>
    val artists: List<Artist>
    val albums: List<Album>
    val playlists: List<Playlist>

    fun sortedSongs(musicConfig: MusicConfig): List<Song>
    fun sortedArtists(musicConfig: MusicConfig): List<Artist>
    fun sortedAlbums(musicConfig: MusicConfig): List<Album>
    fun sortedPlaylist(musicConfig: MusicConfig): List<Playlist>

    fun getSong(songId: Long): Song?
    fun getArtist(artistId: Long): Artist?
    fun getAlbum(albumId: Long): Album?
    fun getPlaylist(playlistId: Long): Playlist?
    fun getLyrics(song: Song): Lyrics?

    suspend fun saveQueue(currentQueue: List<Song>, originalQueue: List<Song>, index: Int)
    suspend fun saveProgress(progress: Long)

    suspend fun fetchSongs(musicConfig: MusicConfig? = null)
    suspend fun fetchArtists(musicConfig: MusicConfig? = null)
    suspend fun fetchAlbums(musicConfig: MusicConfig? = null)
    suspend fun fetchPlaylist(musicConfig: MusicConfig? = null)
    suspend fun fetchArtistArtwork()
    suspend fun fetchAlbumArtwork()
    suspend fun fetchLyrics(song: Song)

    suspend fun isFavorite(song: Song): Boolean
    suspend fun addToFavorite(song: Song)
    suspend fun removeFromFavorite(song: Song)

    suspend fun setShuffleMode(mode: ShuffleMode)
    suspend fun setRepeatMode(mode: RepeatMode)

    suspend fun setSongOrder(musicOrder: MusicOrder)
    suspend fun setArtistOrder(musicOrder: MusicOrder)
    suspend fun setAlbumOrder(musicOrder: MusicOrder)
}
