package caios.android.kanade.core.repository

import Ignores
import android.content.Context
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.database.album_detail.AlbumDetailDao
import caios.android.kanade.core.database.album_detail.AlbumDetailEntity
import caios.android.kanade.core.database.album_detail.AlbumDetailModel
import caios.android.kanade.core.database.album_detail.AlbumTagEntity
import caios.android.kanade.core.database.album_detail.AlbumTrackEntity
import caios.android.kanade.core.database.artist_detail.ArtistDetailDao
import caios.android.kanade.core.database.artist_detail.ArtistDetailEntity
import caios.android.kanade.core.database.artist_detail.ArtistDetailModel
import caios.android.kanade.core.database.artist_detail.ArtistTagEntity
import caios.android.kanade.core.database.artist_detail.SimilarArtistEntity
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity
import caios.android.kanade.core.model.entity.LastFmAlbumDetailEntity
import caios.android.kanade.core.model.entity.LastFmArtistDetailEntity
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.AlbumDetail
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.ArtistDetail
import caios.android.kanade.core.model.music.FmTag
import caios.android.kanade.core.repository.util.parse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.jsoup.Jsoup
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class DefaultLastFmRepository @Inject constructor(
    private val client: HttpClient,
    private val artistRepository: ArtistRepository,
    private val albumRepository: AlbumRepository,
    private val artistDetailDao: ArtistDetailDao,
    private val albumDetailDao: AlbumDetailDao,
    private val artworkDao: ArtworkDao,
    private val kanadeConfig: KanadeConfig,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
) : LastFmRepository {

    private val formatter = Json { ignoreUnknownKeys = true }

    private val _artistDetail = MutableStateFlow(mutableMapOf<Long, ArtistDetail>())
    private val _albumDetail = MutableStateFlow(mutableMapOf<Long, AlbumDetail>())

    override val artistDetails: SharedFlow<Map<Long, ArtistDetail>> = _artistDetail.asSharedFlow()
    override val albumDetails: SharedFlow<Map<Long, AlbumDetail>> = _albumDetail.asSharedFlow()

    override fun getArtistDetails(): Map<Long, ArtistDetail> = _artistDetail.value.toMap()
    override fun getAlbumDetails(): Map<Long, AlbumDetail> = _albumDetail.value.toMap()

    override suspend fun artistDetail(artist: Artist): ArtistDetail? = withContext(dispatcher) {
        _artistDetail.value[artist.artistId] ?: kotlin.runCatching {
            fetchArtistInfo(artist.artist)?.let {
                ArtistDetail(
                    data = artist,
                    mbid = it.artist.mbid,
                    url = it.artist.url,
                    imageUrl = fetchArtistArtwork(it.artist.url),
                    tags = it.artist.tags.tag.map { tag -> FmTag(name = tag.name, url = tag.url) },
                    similarArtists = it.artist.similar.artist.map { artist ->
                        ArtistDetail.SimilarArtist(
                            name = artist.name,
                            url = artist.url,
                            imageUrl = artist.image.findLast { image -> image.text.isNotBlank() }?.text,
                        )
                    },
                    biography = it.artist.biography.content.replace(Regex("<a.*>"), "").ifBlank { null },
                )
            }
        }.getOrNull()?.also {
            saveArtistDetail(it)
            _artistDetail.value = _artistDetail.value.toMutableMap().apply { this[artist.artistId] = it }
        }
    }

    override suspend fun albumDetail(album: Album): AlbumDetail? = withContext(dispatcher) {
        _albumDetail.value[album.albumId] ?: kotlin.runCatching {
            fetchAlbumInfo(album.artist, album.album)?.let {
                AlbumDetail(
                    data = album,
                    mbid = it.album.mbid,
                    imageUrl = it.album.images.findLast { image -> image.text.isNotBlank() }?.text,
                    tags = it.album.tags.tag.map { tag -> FmTag(tag.name, tag.url) },
                    tracks = it.album.tracks.track.map { track ->
                        AlbumDetail.Track(
                            track = track.attr.rank,
                            musicName = track.name,
                            url = track.url,
                        )
                    },
                )
            }
        }.getOrNull()?.also {
            saveAlbumDetail(it)
            _albumDetail.value = _albumDetail.value.toMutableMap().apply { this[album.albumId] = it }
        }
    }

    override suspend fun fetchArtistDetails(): Unit = withContext(dispatcher) {
        artistDetailDao.loadAll().map { it.toData() }.onEach { detail ->
            if (detail != null) {
                _artistDetail.value[detail.data.artistId] = detail
            }
        }
    }

    override suspend fun fetchAlbumDetails(): Unit = withContext(dispatcher) {
        albumDetailDao.loadAll().map { it.toData() }.onEach { detail ->
            if (detail != null) {
                _albumDetail.value[detail.data.albumId] = detail
            }
        }
    }

    private suspend fun fetchAlbumInfo(artistName: String, albumName: String): LastFmAlbumDetailEntity? {
        return try {
            client.get {
                url(ENDPOINT)
                parameter("api_key", kanadeConfig.lastFmApiKey)
                parameter("lang", "ja")
                parameter("autocorrect", 1)
                parameter("format", "json")
                parameter("artist", artistName)
                parameter("album", albumName)
                parameter("method", "album.getinfo")
            }.parse()
        } catch (e: Throwable) {
            Timber.w(e)
            null
        }
    }

    private suspend fun fetchArtistInfo(artistName: String): LastFmArtistDetailEntity? {
        return try {
            client.get {
                url(ENDPOINT)
                parameter("api_key", kanadeConfig.lastFmApiKey)
                parameter("lang", "ja")
                parameter("autocorrect", 1)
                parameter("format", "json")
                parameter("artist", artistName)
                parameter("method", "artist.getinfo")
            }.parse()
        } catch (e: Throwable) {
            Timber.w(e)
            null
        }
    }

    private suspend fun fetchArtistArtwork(artistFmUrl: String): String? {
        return runCatching {
            val html = client.get(artistFmUrl).bodyAsText()
            val doc = Jsoup.parse(html)

            doc.selectFirst(".header-new-background-image")?.attr("content")
        }.getOrNull()
    }

    override fun setIgnoreArtist(artist: Artist) {
        val ignores = getIgnores()
        val newIgnores = ignores.copy(artistIds = ignores.artistIds.toMutableList().apply { add(artist.artistId) })

        saveIgnores(newIgnores)
    }

    override fun setIgnoreAlbum(album: Album) {
        val ignores = getIgnores()
        val newIgnores = ignores.copy(albumIds = ignores.albumIds.toMutableList().apply { add(album.albumId) })

        saveIgnores(newIgnores)
    }

    override fun getIgnores(): Ignores {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return Ignores(emptyList(), emptyList())

        return formatter.decodeFromString(Ignores.serializer(), file.readText())
    }

    private fun saveIgnores(ignores: Ignores) {
        val file = File(context.filesDir, FILE_NAME)
        file.writeText(formatter.encodeToString(Ignores.serializer(), ignores))
    }

    private suspend fun saveArtistDetail(artistDetail: ArtistDetail) = withContext(dispatcher) {
        val entity = ArtistDetailEntity(
            id = 0,
            artistId = artistDetail.data.artistId,
            artistName = artistDetail.data.artist,
            mbid = artistDetail.mbid,
            url = artistDetail.url,
            imageUrl = artistDetail.imageUrl,
            biography = artistDetail.biography,
        )

        val similarArtists = artistDetail.similarArtists.map { artist ->
            SimilarArtistEntity(
                id = 0,
                artistId = artistDetail.data.artistId,
                name = artist.name,
                url = artist.url,
                imageUrl = artist.imageUrl,
            )
        }

        val tags = artistDetail.tags.map { tag ->
            ArtistTagEntity(
                id = 0,
                artistId = artistDetail.data.artistId,
                name = tag.name,
                url = tag.url,
            )
        }

        artistDetailDao.delete(artistDetail.data.artistId)
        artistDetailDao.insertArtistDetail(entity)
        artistDetailDao.insertSimilarArtist(*similarArtists.toTypedArray())
        artistDetailDao.insertArtistTag(*tags.toTypedArray())

        val artwork = artworkDao.loadArtist(artistDetail.data.artistId)

        if (artwork == null) {
            artworkDao.insert(
                ArtworkEntity(
                    id = 0,
                    artistId = artistDetail.data.artistId,
                    web = artistDetail.imageUrl,
                ),
            )
        } else {
            artworkDao.update(artwork.copy(web = artistDetail.imageUrl))
        }
    }

    private suspend fun saveAlbumDetail(albumDetail: AlbumDetail) = withContext(dispatcher) {
        val entity = AlbumDetailEntity(
            id = 0,
            albumId = albumDetail.data.albumId,
            artistName = albumDetail.data.artist,
            albumName = albumDetail.data.album,
            mbid = albumDetail.mbid,
            imageUrl = albumDetail.imageUrl,
        )

        val tracks = albumDetail.tracks.map { track ->
            AlbumTrackEntity(
                id = 0,
                albumId = albumDetail.data.albumId,
                track = track.track,
                name = track.musicName,
                url = track.url,
            )
        }

        val tags = albumDetail.tags.map { tag ->
            AlbumTagEntity(
                id = 0,
                albumId = albumDetail.data.albumId,
                name = tag.name,
                url = tag.url,
            )
        }

        albumDetailDao.delete(albumDetail.data.albumId)
        albumDetailDao.insertAlbumDetail(entity)
        albumDetailDao.insertTrack(*tracks.toTypedArray())
        albumDetailDao.insertTag(*tags.toTypedArray())

        val artwork = artworkDao.loadAlbum(albumDetail.data.albumId)

        if (artwork == null) {
            artworkDao.insert(
                ArtworkEntity(
                    id = 0,
                    albumId = albumDetail.data.albumId,
                    web = albumDetail.imageUrl,
                ),
            )
        } else {
            artworkDao.update(artwork.copy(web = albumDetail.imageUrl))
        }
    }

    private fun ArtistDetailModel.toData(): ArtistDetail? {
        val artist = artistRepository.get(artistDetail.artistId) ?: return null
        return ArtistDetail(
            data = artist,
            mbid = artistDetail.mbid,
            url = artistDetail.url,
            imageUrl = artistDetail.imageUrl,
            tags = tags.map { tag -> FmTag(tag.name, tag.url) },
            similarArtists = similarArtists.map { data ->
                ArtistDetail.SimilarArtist(
                    name = data.name,
                    url = data.url,
                    imageUrl = data.imageUrl,
                )
            },
            biography = artistDetail.biography,
        )
    }

    private fun AlbumDetailModel.toData(): AlbumDetail? {
        val album = albumRepository.get(albumDetail.albumId) ?: return null
        return AlbumDetail(
            data = album,
            mbid = albumDetail.mbid,
            imageUrl = albumDetail.imageUrl,
            tags = tags.map { tag -> FmTag(tag.name, tag.url) },
            tracks = tracks.map { track ->
                AlbumDetail.Track(
                    track = track.track,
                    url = track.url,
                    musicName = track.name,
                )
            },
        )
    }

    companion object {
        private const val ENDPOINT = "http://ws.audioscrobbler.com/2.0/"
        private const val FILE_NAME = "MusicDetailIgnores.json"
    }
}
