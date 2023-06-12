package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.Flow

interface MusicRepository {

    val config: Flow<MusicConfig>
    val lastQueue: Flow<LastQueue>

    val songs: List<Song>
    val artists: List<Artist>
    val albums: List<Album>

    fun sortedSongs(musicConfig: MusicConfig): List<Song>
    fun sortedArtists(musicConfig: MusicConfig): List<Artist>
    fun sortedAlbums(musicConfig: MusicConfig): List<Album>

    suspend fun saveQueue(items: List<Long>, index: Int, isShuffled: Boolean)
    suspend fun saveProgress(progress: Long)

    suspend fun fetchSongs(musicConfig: MusicConfig)
    suspend fun fetchArtists(musicConfig: MusicConfig)
    suspend fun fetchAlbums(musicConfig: MusicConfig)
    suspend fun fetchArtistArtwork()
    suspend fun fetchAlbumArtwork()

    suspend fun setShuffleMode(mode: ShuffleMode)
    suspend fun setRepeatMode(mode: RepeatMode)

    suspend fun setSongOrder(musicOrder: MusicOrder)
    suspend fun setArtistOrder(musicOrder: MusicOrder)
    suspend fun setAlbumOrder(musicOrder: MusicOrder)
}
