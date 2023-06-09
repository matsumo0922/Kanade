package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface MusicRepository {

    val config: Flow<MusicConfig>
    val songs: SharedFlow<List<Song>>
    val artists: SharedFlow<List<Artist>>
    val albums: SharedFlow<List<Album>>

    suspend fun fetchSongs(musicConfig: MusicConfig)
    suspend fun fetchArtists(musicConfig: MusicConfig)
    suspend fun fetchAlbums(musicConfig: MusicConfig)

    suspend fun setShuffleMode(mode: ShuffleMode)
    suspend fun setRepeatMode(mode: RepeatMode)

    suspend fun setSongOrder(musicOrder: MusicOrder)
    suspend fun setArtistOrder(musicOrder: MusicOrder)
    suspend fun setAlbumOrder(musicOrder: MusicOrder)
}
