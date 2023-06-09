package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.MusicOrderOption
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.sortList
import javax.inject.Inject

class DefaultArtistRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artworkRepository: ArtworkRepository,
) : ArtistRepository {

    override suspend fun artist(artistId: Long, musicConfig: MusicConfig): Artist {
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
            artwork = artworkRepository.artistArtwork(artistId),
        )
    }

    override suspend fun artists(musicConfig: MusicConfig): List<Artist> {
        val cursor = songRepository.makeCursor(
            selection = "",
            selectionValues = emptyList(),
            musicOrders = getSongLoaderOrder(musicConfig),
        )
        val songs = songRepository.songs(cursor)

        return splitIntoArtists(songs, musicConfig)
    }

    override suspend fun artists(query: String, musicConfig: MusicConfig): List<Artist> {
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
                    artwork = artworkRepository.artistArtworks[it.key] ?: Artwork.Unknown,
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
