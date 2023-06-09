package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject

class DefaultMusicRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val artworkRepository: ArtworkRepository,
) : MusicRepository {

    private val _songs: MutableSharedFlow<List<Song>> = MutableSharedFlow(1)
    private val _artists: MutableSharedFlow<List<Artist>> = MutableSharedFlow(1)
    private val _albums: MutableSharedFlow<List<Album>> = MutableSharedFlow(1)
    private val _artistArtworks: MutableSharedFlow<Map<Long, Artwork>> = MutableSharedFlow(1)
    private val _albumArtworks: MutableSharedFlow<Map<Long, Artwork>> = MutableSharedFlow(1)

    override val config: Flow<MusicConfig> = kanadePreferencesDataStore.musicConfig
    override val songs: SharedFlow<List<Song>> = _songs
    override val artists: SharedFlow<List<Artist>> = _artists
    override val albums: SharedFlow<List<Album>> = _albums
    override val artistArtworks: SharedFlow<Map<Long, Artwork>> = _artistArtworks
    override val albumArtworks: SharedFlow<Map<Long, Artwork>> = _albumArtworks

    override suspend fun fetchSongs(musicConfig: MusicConfig) {
        _songs.emit(songRepository.songs(musicConfig))
    }

    override suspend fun fetchArtists(musicConfig: MusicConfig) {
        var data = artistRepository.artists(musicConfig)
        val result = artworkRepository.fetchArtistArtwork(data)

        if (result) {
            data = artistRepository.artists(musicConfig)
        }

        _artists.emit(data)
    }

    override suspend fun fetchAlbums(musicConfig: MusicConfig) {
        var data = albumRepository.albums(musicConfig)
        val result = artworkRepository.fetchAlbumArtwork(data)

        if (result) {
            data = albumRepository.albums(musicConfig)
        }

        _albums.emit(data)
    }

    override suspend fun fetchArtistArtwork() {
        _artistArtworks.emit(artworkRepository.artistArtworks())
    }

    override suspend fun fetchAlbumArtwork() {
        _albumArtworks.emit(artworkRepository.albumArtworks())
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
