package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.Album
import caios.android.kanade.core.model.Artist
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.RepeatMode
import caios.android.kanade.core.model.ShuffleMode
import caios.android.kanade.core.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class DefaultMusicRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
) : MusicRepository {

    override val config: Flow<MusicConfig> = kanadePreferencesDataStore.musicConfig
    override val songs: MutableSharedFlow<List<Song>> = MutableSharedFlow(1, 1)
    override val artists: MutableSharedFlow<List<Artist>> = MutableSharedFlow(1)
    override val albums: MutableSharedFlow<List<Album>> = MutableSharedFlow(1)

    override suspend fun fetchSongs() {
        config.collect {
            songs.emit(songRepository.songs(it))
        }
    }

    override suspend fun fetchArtists() {
        config.collect {
            artists.emit(artistRepository.artists(it))
        }
    }

    override suspend fun fetchAlbums() {
        config.collect {
            albums.emit(albumRepository.albums(it))
        }
    }

    override suspend fun setShuffleMode(mode: ShuffleMode) {
        kanadePreferencesDataStore.setShuffleMode(mode)
    }

    override suspend fun setRepeatMode(mode: RepeatMode) {
        kanadePreferencesDataStore.setRepeatMode(mode)
    }

    override suspend fun setSongOrder(musicOrder: MusicOrder) {
        kanadePreferencesDataStore.setSongOrder(musicOrder)
    }

    override suspend fun setArtistOrder(musicOrder: MusicOrder) {
        kanadePreferencesDataStore.setArtistOrder(musicOrder)
    }

    override suspend fun setAlbumOrder(musicOrder: MusicOrder) {
        kanadePreferencesDataStore.setAlbumOrder(musicOrder)
    }
}
