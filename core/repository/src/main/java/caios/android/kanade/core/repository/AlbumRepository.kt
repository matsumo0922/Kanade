package caios.android.kanade.core.repository

import android.provider.MediaStore
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.repository.util.sortList
import okhttp3.internal.toImmutableMap
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

interface AlbumRepository {

    fun clear()

    fun get(albumId: Long): Album?
    fun gets(albumIds: List<Long>): List<Album>
    fun gets(): List<Album>

    suspend fun album(albumId: Long, musicConfig: MusicConfig): Album
    suspend fun albums(musicConfig: MusicConfig): List<Album>
    suspend fun albums(query: String, musicConfig: MusicConfig): List<Album>

    fun splitIntoAlbums(songs: List<Song>, musicConfig: MusicConfig): List<Album>
    fun fetchArtwork()
    fun albumsSort(albums: List<Album>, musicConfig: MusicConfig): List<Album>
}

class AlbumRepositoryImpl @Inject constructor(
    private val songRepository: SongRepository,
    private val artworkRepository: ArtworkRepository,
) : AlbumRepository {

    private val cache = ConcurrentHashMap<Long, Album>()

    override fun clear() {
        cache.clear()
    }

    override fun get(albumId: Long): Album? = cache[albumId]

    override fun gets(albumIds: List<Long>): List<Album> = albumIds.mapNotNull { get(it) }

    override fun gets(): List<Album> = cache.values.toList()

    override suspend fun album(albumId: Long, musicConfig: MusicConfig): Album {
        val albumArtworks = artworkRepository.albumArtworks.toImmutableMap()
        val cursor = songRepository.makeCursor(
            selection = MediaStore.Audio.AudioColumns.ALBUM_ID + "=?",
            selectionValues = listOf(albumId.toString()),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return Album(
            album = songs.firstOrNull()?.album ?: "",
            albumId = albumId,
            songs = songs,
            artwork = albumArtworks[albumId] ?: Artwork.Unknown,
        )
    }

    override suspend fun albums(musicConfig: MusicConfig): List<Album> {
        val cursor = songRepository.makeCursor(
            selection = "",
            selectionValues = emptyList(),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig)
    }

    override suspend fun albums(query: String, musicConfig: MusicConfig): List<Album> {
        val cursor = songRepository.makeCursor(
            selection = MediaStore.Audio.AudioColumns.ALBUM + " LIKE ?",
            selectionValues = listOf("%$query%"),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig)
    }

    override fun splitIntoAlbums(songs: List<Song>, musicConfig: MusicConfig): List<Album> {
        val albumArtworks = artworkRepository.albumArtworks.toImmutableMap()
        val albums = songs
            .groupBy { it.albumId }
            .map { (albumId, songs) ->
                Album(
                    album = songs.first().album,
                    albumId = albumId,
                    songs = songs,
                    artwork = albumArtworks[albumId] ?: Artwork.Unknown,
                ).also {
                    cache[albumId] = it
                }
            }

        return albumsSort(albums, musicConfig)
    }

    override fun fetchArtwork() {
        for ((albumId, artwork) in artworkRepository.albumArtworks.toImmutableMap()) {
            val data = cache[albumId] ?: continue
            if (data.artwork == artwork) continue

            val songs = data.songs.mapNotNull { songRepository.get(it.id) }

            cache[albumId] = data.copy(
                artwork = artwork,
                songs = songs,
            )
        }
    }

    override fun albumsSort(albums: List<Album>, musicConfig: MusicConfig): List<Album> {
        val order = musicConfig.albumOrder
        val option = order.option

        require(option is MusicOrderOption.Album) { "MusicOrderOption is not Album" }

        return when (option) {
            MusicOrderOption.Album.NAME -> albums.sortList({ it.album }, order = order.order)
            MusicOrderOption.Album.TRACKS -> albums.sortList({ it.songs.size }, { it.album }, order = order.order)
            MusicOrderOption.Album.ARTIST -> albums.sortList({ it.artist }, { it.album }, order = order.order)
            MusicOrderOption.Album.YEAR -> albums.sortList({ it.year }, { it.album }, order = order.order)
        }
    }

    private fun getSongLoaderOrder(musicConfig: MusicConfig): Array<MusicOrder> {
        return arrayOf(musicConfig.songOrder)
    }
}
