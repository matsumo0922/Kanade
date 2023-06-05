package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import javax.inject.Inject

class DefaultMusicRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val artworkRepository: ArtworkRepository,
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
            val data = artistRepository.artists(it)

            artists.emit(data)

            if (artworkRepository.fetchArtistArtwork(data)) {
                Timber.d("Required refetch artists.")
                fetchArtists()
            }
        }
    }

    override suspend fun fetchAlbums() {
        config.collect {
            val data = albumRepository.albums(it)

            albums.emit(data)

            if (artworkRepository.fetchAlbumArtwork(data)) {
                Timber.d("Required refetch songs and albums.")
                fetchSongs()
                fetchAlbums()
            }
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
