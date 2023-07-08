package caios.android.kanade.core.repository

import android.provider.MediaStore.Audio.AudioColumns
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.repository.util.sortList
import okhttp3.internal.toImmutableMap
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DefaultArtistRepository @Inject constructor(
    private val songRepository: SongRepository,
    private val albumRepository: AlbumRepository,
    private val artworkRepository: ArtworkRepository,
) : ArtistRepository {

    private val cache = ConcurrentHashMap<Long, Artist>()

    override fun get(artistId: Long): Artist? = cache[artistId]

    override fun gets(artistIds: List<Long>): List<Artist> = artistIds.mapNotNull { get(it) }

    override fun gets(): List<Artist> = cache.values.toList()

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
            albums = albumRepository.splitIntoAlbums(songs, musicConfig),
            artwork = artworkRepository.artistArtworks[artistId] ?: Artwork.Unknown,
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
        val albums = albumRepository.splitIntoAlbums(songs, musicConfig)
        val artists = albums
            .groupBy { it.artistId }
            .map { (artistId, albums) ->
                Artist(
                    artist = albums.first().artist,
                    artistId = artistId,
                    albums = albums,
                    artwork = artworkRepository.artistArtworks[artistId] ?: Artwork.Unknown,
                ).also {
                    cache[artistId] = it
                }
            }

        return artistsSort(artists, musicConfig)
    }

    override fun fetchArtwork() {
        val albumArtworks = artworkRepository.albumArtworks.toImmutableMap()

        for ((artistId, artwork) in artworkRepository.artistArtworks.toImmutableMap()) {
            val data = cache[artistId] ?: continue
            if (data.artwork == artwork) continue

            val albums = data.albums.map {
                it.copy(
                    artwork = albumArtworks[it.albumId] ?: Artwork.Unknown,
                    songs = it.songs.mapNotNull { song -> songRepository.get(song.id) },
                )
            }

            cache[artistId] = data.copy(
                artwork = artwork,
                albums = albums,
            )
        }
    }

    override fun artistsSort(artists: List<Artist>, musicConfig: MusicConfig): List<Artist> {
        val order = musicConfig.artistOrder
        val option = order.option

        require(option is MusicOrderOption.Artist) { "MusicOrderOption is not Artist" }

        return when (option) {
            MusicOrderOption.Artist.NAME -> artists.sortList({ it.artist }, order = order.order)
            MusicOrderOption.Artist.TRACKS -> artists.sortList({ it.songs.size }, order = order.order)
            MusicOrderOption.Artist.ALBUMS -> artists.sortList({ it.albums.size }, order = order.order)
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
