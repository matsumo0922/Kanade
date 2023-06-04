package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.Album
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.MusicOrderOption
import caios.android.kanade.core.model.Song
import caios.android.kanade.core.repository.util.sortList
import javax.inject.Inject

class DefaultAlbumRepository @Inject constructor(
    private val songRepository: SongRepository,
) : AlbumRepository {

    override fun album(albumId: Long, musicConfig: MusicConfig): Album {
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
        )
    }

    override fun albums(musicConfig: MusicConfig): List<Album> {
        val cursor = songRepository.makeCursor(
            selection = "",
            selectionValues = emptyList(),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig.albumOrder)
    }

    override fun albums(query: String, musicConfig: MusicConfig): List<Album> {
        val cursor = songRepository.makeCursor(
            selection = AudioColumns.ALBUM + " LIKE ?",
            selectionValues = listOf("%$query%"),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig.albumOrder)
    }

    override fun splitIntoAlbums(songs: List<Song>, musicOrder: MusicOrder): List<Album> {
        val option = musicOrder.musicOrderOption
        val albums = songs
            .groupBy { it.albumId }
            .map {
                Album(
                    album = it.value.first().album,
                    albumId = it.key,
                    songs = it.value,
                )
            }

        if (option !is MusicOrderOption.Album) {
            throw IllegalArgumentException("MusicOrderOption is not Album")
        }

        return when (option) {
            MusicOrderOption.Album.NAME -> albums.sortList({ it.album }, order = musicOrder.order)
            MusicOrderOption.Album.TRACKS -> albums.sortList({ it.songs.size }, { it.album }, order = musicOrder.order)
            MusicOrderOption.Album.ARTIST -> albums.sortList({ it.artist }, { it.album }, order = musicOrder.order)
            MusicOrderOption.Album.YEAR -> albums.sortList({ it.year }, { it.album }, order = musicOrder.order)
        }
    }

    private fun getSongLoaderOrder(musicConfig: MusicConfig): Array<MusicOrder> {
        return arrayOf(musicConfig.albumOrder, musicConfig.songOrder)
    }
}
