package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.Artist
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.MusicOrderOption
import caios.android.kanade.core.model.Song
import caios.android.kanade.core.repository.util.sortList
import javax.inject.Inject

class DefaultArtistRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
) : ArtistRepository {

    override fun artist(artistId: Long, musicConfig: MusicConfig): Artist {
        val cursor = songRepository.makeCursor(
            selection = AudioColumns.ARTIST_ID + "=?",
            selectionValues = listOf(artistId.toString()),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return Artist(
            artist = songs.firstOrNull()?.artist ?: "",
            artistId = artistId,
            albums = albumRepository.splitIntoAlbums(songs, musicConfig.albumOrder),
        )
    }

    override fun artists(musicConfig: MusicConfig): List<Artist> {
        val cursor = songRepository.makeCursor(
            selection = "",
            selectionValues = emptyList(),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoArtists(songs, musicConfig)
    }

    override fun artists(query: String, musicConfig: MusicConfig): List<Artist> {
        val cursor = songRepository.makeCursor(
            selection = AudioColumns.ARTIST + " LIKE ?",
            selectionValues = listOf("%$query%"),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoArtists(songs, musicConfig)
    }

    override fun splitIntoArtists(songs: List<Song>, musicConfig: MusicConfig): List<Artist> {
        val artistOrder = musicConfig.artistOrder
        val option = artistOrder.musicOrderOption
        val albums = albumRepository.splitIntoAlbums(songs, musicConfig.albumOrder)
        val artists = albums
            .groupBy { it.artistId }
            .map {
                Artist(
                    artist = it.value.first().artist,
                    artistId = it.key,
                    albums = it.value,
                )
            }

        if (option !is MusicOrderOption.Artist) {
            throw IllegalArgumentException("MusicOrderOption is not Artist")
        }

        return when (option) {
            MusicOrderOption.Artist.NAME -> artists.sortList({ it.artist }, order = artistOrder.order)
            MusicOrderOption.Artist.TRACKS -> artists.sortList({ it.songs.size }, order = artistOrder.order)
            MusicOrderOption.Artist.ALBUMS -> artists.sortList({ it.albums.size }, order = artistOrder.order)
        }
    }

    private fun getSongLoaderOrder(musicConfig: MusicConfig): Array<MusicOrder> {
        return arrayOf(
            musicConfig.artistOrder,
            musicConfig.albumOrder,
            musicConfig.songOrder,
        )
    }
}
