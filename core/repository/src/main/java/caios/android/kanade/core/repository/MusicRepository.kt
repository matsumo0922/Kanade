package caios.android.kanade.core.repository

import caios.android.kanade.core.model.Album
import caios.android.kanade.core.model.Artist
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.RepeatMode
import caios.android.kanade.core.model.ShuffleMode
import caios.android.kanade.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MusicRepository {

    val config: Flow<MusicConfig>
    val songs: SharedFlow<List<Song>>
    val artists: SharedFlow<List<Artist>>
    val albums: SharedFlow<List<Album>>

    suspend fun fetchSongs()
    suspend fun fetchArtists()
    suspend fun fetchAlbums()

    suspend fun setShuffleMode(mode: ShuffleMode)
    suspend fun setRepeatMode(mode: RepeatMode)

    suspend fun setSongOrder(musicOrder: MusicOrder)
    suspend fun setArtistOrder(musicOrder: MusicOrder)
    suspend fun setAlbumOrder(musicOrder: MusicOrder)
}
