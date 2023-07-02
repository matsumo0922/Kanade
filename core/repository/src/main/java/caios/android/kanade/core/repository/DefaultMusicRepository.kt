package caios.android.kanade.core.repository

import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.KanadePreferencesDataStore
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.LastQueue
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.PlaylistItem
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.RepeatMode
import caios.android.kanade.core.model.player.ShuffleMode
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultMusicRepository @Inject constructor(
    private val kanadePreferencesDataStore: KanadePreferencesDataStore,
    private val songRepository: SongRepository,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val playlistRepository: PlaylistRepository,
    private val artworkRepository: ArtworkRepository,
    private val lyricsRepository: LyricsRepository,
    @Dispatcher(KanadeDispatcher.Main) private val main: CoroutineDispatcher,
) : MusicRepository {

    override val config: Flow<MusicConfig> = kanadePreferencesDataStore.musicConfig.flowOn(main)
    override val lastQueue: Flow<LastQueue> = kanadePreferencesDataStore.lastQueue.flowOn(main)

    override val songs: List<Song> get() = songRepository.gets()
    override val artists: List<Artist> get() = artistRepository.gets()
    override val albums: List<Album> get() = albumRepository.gets()
    override val playlists: List<Playlist> get() = playlistRepository.gets()

    override fun sortedSongs(musicConfig: MusicConfig): List<Song> {
        return songRepository.songsSort(songs, musicConfig)
    }

    override fun sortedArtists(musicConfig: MusicConfig): List<Artist> {
        return artistRepository.artistsSort(artists, musicConfig)
    }

    override fun sortedAlbums(musicConfig: MusicConfig): List<Album> {
        return albumRepository.albumsSort(albums, musicConfig)
    }

    override fun sortedPlaylists(musicConfig: MusicConfig): List<Playlist> {
        return playlistRepository.playlistSort(playlists, musicConfig)
    }

    override fun getSong(songId: Long): Song? {
        return songRepository.get(songId) ?: songs.find { it.id == songId }
    }

    override fun getArtist(artistId: Long): Artist? {
        return artistRepository.get(artistId) ?: artists.find { it.artistId == artistId }
    }

    override fun getAlbum(albumId: Long): Album? {
        return albumRepository.get(albumId) ?: albums.find { it.albumId == albumId }
    }

    override fun getPlaylist(playlistId: Long): Playlist? {
        return playlistRepository.get(playlistId) ?: playlists.find { it.id == playlistId }
    }

    override fun getLyrics(song: Song): Lyrics? {
        return lyricsRepository.get(song)
    }

    override suspend fun saveQueue(currentQueue: List<Song>, originalQueue: List<Song>, index: Int) {
        kanadePreferencesDataStore.setLastQueue(
            currentItems = currentQueue.map { it.id },
            originalItems = originalQueue.map { it.id },
            index = index,
        )
    }

    override suspend fun saveProgress(progress: Long) {
        kanadePreferencesDataStore.setLastQueueProgress(progress)
    }

    override suspend fun fetchSongs(musicConfig: MusicConfig?) {
        songRepository.songs(musicConfig ?: config.first())
    }

    override suspend fun fetchArtists(musicConfig: MusicConfig?) {
        artistRepository.artists(musicConfig ?: config.first())
    }

    override suspend fun fetchAlbums(musicConfig: MusicConfig?) {
        albumRepository.albums(musicConfig ?: config.first())
    }

    override suspend fun fetchPlaylist(musicConfig: MusicConfig?) {
        playlistRepository.playlists(musicConfig ?: config.first())
    }

    override suspend fun fetchArtistArtwork() {
        artworkRepository.fetchArtistArtwork(artists)
        artistRepository.fetchArtwork()
    }

    override suspend fun fetchAlbumArtwork() {
        artworkRepository.fetchAlbumArtwork(albums)
        songRepository.fetchArtwork()
        albumRepository.fetchArtwork()
    }

    override suspend fun fetchLyrics(song: Song) {
        lyricsRepository.lyrics(song)
    }

    override suspend fun createPlaylist(name: String, songs: List<Song>) {
        val items = songs.mapIndexed { index, song -> PlaylistItem(0, song, index) }
        val playlist = Playlist(0, name, items.toSet())

        playlistRepository.create(playlist)
    }

    override suspend fun removePlaylist(playlist: Playlist) {
        playlistRepository.remove(playlist)
    }

    override suspend fun addToPlaylist(playlist: Playlist, songs: List<Song>) {
        playlistRepository.addItems(playlist.id, songs)
    }

    override suspend fun removeFromPlaylist(playlist: Playlist, index: Int) {
        playlistRepository.removeItem(playlist.id, index)
    }

    override suspend fun isFavorite(song: Song): Boolean {
        return playlistRepository.isFavorite(song)
    }

    override suspend fun addToFavorite(song: Song) {
        playlistRepository.addToFavorite(song)
    }

    override suspend fun removeFromFavorite(song: Song) {
        playlistRepository.removeFromFavorite(song)
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
