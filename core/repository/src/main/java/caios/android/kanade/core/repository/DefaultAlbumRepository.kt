package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.MusicOrderOption
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.sortList
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DefaultAlbumRepository @Inject constructor(
    private val songRepository: SongRepository,
) : AlbumRepository {

    private val cache = ConcurrentHashMap<Long, Album>()

    override fun get(albumId: Long): Album? = cache[albumId]

    override fun gets(albumIds: List<Long>): List<Album> = albumIds.mapNotNull { get(it) }

    override fun gets(): List<Album> = cache.values.toList()

    override suspend fun album(albumId: Long, musicConfig: MusicConfig): Album {
        val cursor = songRepository.makeCursor(
            selection = AudioColumns.ALBUM_ID + "=?",
            selectionValues = listOf(albumId.toString()),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return Album(
            album = songs.firstOrNull()?.album ?: "",
            albumId = albumId,
            songs = songs,
            artwork = Artwork.Unknown,
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
            selection = AudioColumns.ALBUM + " LIKE ?",
            selectionValues = listOf("%$query%"),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig)
    }

    override fun splitIntoAlbums(songs: List<Song>, musicConfig: MusicConfig): List<Album> {
        val albums = songs
            .groupBy { it.albumId }
            .map { (albumId, songs) ->
                Album(
                    album = songs.first().album,
                    albumId = albumId,
                    songs = songs,
                    artwork = Artwork.Unknown,
                ).also {
                    cache[albumId] = it
                }
            }

        return albumsSort(albums, musicConfig)
    }

    override fun applyArtwork(albumId: Long, artwork: Artwork) {
        cache[albumId] = cache[albumId]?.copy(artwork = artwork) ?: return
    }

    override fun albumsSort(albums: List<Album>, musicConfig: MusicConfig): List<Album> {
        val order = musicConfig.albumOrder
        val option = order.musicOrderOption

        if (option !is MusicOrderOption.Album) {
            throw IllegalArgumentException("MusicOrderOption is not Album")
        }

        return when (option) {
            MusicOrderOption.Album.NAME -> albums.sortList({ it.album }, order = order.order)
            MusicOrderOption.Album.TRACKS -> albums.sortList({ it.songs.size }, { it.album }, order = order.order)
            MusicOrderOption.Album.ARTIST -> albums.sortList({ it.artist }, { it.album }, order = order.order)
            MusicOrderOption.Album.YEAR -> albums.sortList({ it.year }, { it.album }, order = order.order)
        }
    }

    private fun getSongLoaderOrder(musicConfig: MusicConfig): Array<MusicOrder> {
        return arrayOf(musicConfig.albumOrder, musicConfig.songOrder)
    }
}
