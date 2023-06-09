package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.MusicOrderOption
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.sortList
import javax.inject.Inject

class DefaultAlbumRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val artworkRepository: ArtworkRepository,
) : AlbumRepository {

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
            artwork = artworkRepository.albumArtwork(albumId),
        )
    }

    override suspend fun albums(musicConfig: MusicConfig): List<Album> {
        val cursor = songRepository.makeCursor(
            selection = "",
            selectionValues = emptyList(),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoAlbums(songs, musicConfig.albumOrder)
    }

    override suspend fun albums(query: String, musicConfig: MusicConfig): List<Album> {
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
                    artwork = artworkRepository.albumArtworks[it.key] ?: Artwork.Unknown,
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
