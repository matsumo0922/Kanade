package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.RepeatMode
import caios.android.kanade.core.model.music.ShuffleMode
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultMusicRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val artworkRepository: ArtworkRepository,
) : MusicRepository {

    override val config: Flow<MusicConfig> = kanadePreferencesDataStore.musicConfig
    override val lastQueue: Flow<LastQueue> = kanadePreferencesDataStore.lastQueue

    override val songs: List<Song> get() = songRepository.gets()
    override val artists: List<Artist> get() = artistRepository.gets()
    override val albums: List<Album> get() = albumRepository.gets()

    override fun sortedSongs(musicConfig: MusicConfig): List<Song> {
        return songRepository.songsSort(songs, musicConfig)
    }

    override fun sortedArtists(musicConfig: MusicConfig): List<Artist> {
        return artistRepository.artistsSort(artists, musicConfig)
    }

    override fun sortedAlbums(musicConfig: MusicConfig): List<Album> {
        return albumRepository.albumsSort(albums, musicConfig)
    }

    override suspend fun saveQueue(items: List<Long>, index: Int, isShuffled: Boolean) {
        if (isShuffled) {
            kanadePreferencesDataStore.setLastCurrentQueue(
                currentItems = items,
                index = index,
            )
        } else {
            kanadePreferencesDataStore.setLastOriginalQueue(
                originalItems = items,
                index = index,
            )
        }
    }

    override suspend fun saveProgress(progress: Long) {
        kanadePreferencesDataStore.setLastQueueProgress(progress)
    }

    override suspend fun fetchSongs(musicConfig: MusicConfig) {
        songRepository.songs(musicConfig)
    }

    override suspend fun fetchArtists(musicConfig: MusicConfig) {
        artistRepository.artists(musicConfig)
    }

    override suspend fun fetchAlbums(musicConfig: MusicConfig) {
        albumRepository.albums(musicConfig)
    }

    override suspend fun fetchArtistArtwork() {
        artworkRepository.fetchArtistArtwork(artists)
    }

    override suspend fun fetchAlbumArtwork() {
        artworkRepository.fetchAlbumArtwork(albums)
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
