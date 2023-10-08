package caios.android.kanade.core.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

interface ArtworkRepository {

    val artistArtworks: Map<Long, Artwork>
    val albumArtworks: Map<Long, Artwork>

    fun clear()

    suspend fun artistArtwork(artistId: Long): Artwork
    suspend fun albumArtwork(albumId: Long): Artwork

    suspend fun artistArtworks(artistIds: List<Long>): Map<Long, Artwork>
    suspend fun albumArtworks(albumIds: List<Long>): Map<Long, Artwork>

    suspend fun artistArtworks(): Map<Long, Artwork>
    suspend fun albumArtworks(): Map<Long, Artwork>

    suspend fun fetchArtistArtwork(artists: List<Artist>): Boolean
    suspend fun fetchAlbumArtwork(albums: List<Album>): Boolean
}

class ArtworkRepositoryImpl @Inject constructor(
    private val artworkDao: ArtworkDao,
    @ApplicationContext private val context: Context,
    @Dispatcher(KanadeDispatcher.IO) private val dispatcher: CoroutineDispatcher,
) : ArtworkRepository {

    private var _artistArtwork = mutableMapOf<Long, Artwork>()
    private var _albumArtwork = mutableMapOf<Long, Artwork>()

    override val artistArtworks: Map<Long, Artwork>
        get() = _artistArtwork.toMap()

    override val albumArtworks: Map<Long, Artwork>
        get() = _albumArtwork.toMap()

    override fun clear() {
        _artistArtwork.clear()
        _albumArtwork.clear()
    }

    override suspend fun artistArtworks(): Map<Long, Artwork> = withContext(dispatcher) {
        artworkDao.loadArtists().associate { entity ->
            entity.artistId!! to entity.toArtwork()
        }
    }

    override suspend fun albumArtworks(): Map<Long, Artwork> = withContext(dispatcher) {
        artworkDao.loadAlbums().associate { entity ->
            entity.albumId!! to entity.toArtwork()
        }
    }

    override suspend fun artistArtworks(artistIds: List<Long>): Map<Long, Artwork> = withContext(dispatcher) {
        artworkDao.loadArtists(artistIds).associate { entity ->
            entity.artistId!! to entity.toArtwork()
        }
    }

    override suspend fun albumArtworks(albumIds: List<Long>): Map<Long, Artwork> = withContext(dispatcher) {
        artworkDao.loadAlbums(albumIds).associate { entity ->
            entity.albumId!! to entity.toArtwork()
        }
    }

    override suspend fun artistArtwork(artistId: Long): Artwork = withContext(dispatcher) {
        artworkDao.loadArtist(artistId)?.toArtwork() ?: Artwork.Unknown
    }

    override suspend fun albumArtwork(albumId: Long): Artwork = withContext(dispatcher) {
        artworkDao.loadAlbum(albumId)?.toArtwork() ?: Artwork.Unknown
    }

    override suspend fun fetchArtistArtwork(artists: List<Artist>): Boolean = withContext(dispatcher) {
        val registeredArtworks = artworkDao.loadArtists()
        val registeredIds = registeredArtworks.map { it.artistId }
        val uris = artists.filterNot { registeredIds.contains(it.artistId) }

        if (uris.isEmpty()) {
            Timber.d("Don't necessarily to fetch artist artwork. [fetched=${registeredIds.size}]")

            applyArtistArtworks(artworkDao.loadArtists().associate { it.artistId!! to it.toArtwork() })

            return@withContext false
        }

        val entities = uris.map {
            ArtworkEntity(
                id = 0,
                artistId = it.artistId,
                internal = it.artist,
            )
        }

        artworkDao.insert(*entities.toTypedArray())

        applyArtistArtworks(artworkDao.loadArtists().associate { it.artistId!! to it.toArtwork() })

        return@withContext true
    }

    override suspend fun fetchAlbumArtwork(albums: List<Album>): Boolean = withContext(dispatcher) {
        val registeredIds = artworkDao.loadAlbums().map { it.albumId }
        val uris = albums
            .filterNot { registeredIds.contains(it.albumId) }
            .associateWith { getMediaStoreAlbumCoverUri(it.albumId) }

        if (uris.isEmpty()) {
            Timber.d("Don't necessarily to fetch album artwork. [fetched=${registeredIds.size}]")

            applyAlbumArtworks(artworkDao.loadAlbums().associate { it.albumId!! to it.toArtwork() })

            return@withContext false
        }

        val entities = uris.map {
            ArtworkEntity(
                id = 0,
                albumId = it.key.albumId,
                mediaStore = it.value?.toString(),
                internal = it.key.album,
            )
        }

        artworkDao.insert(*entities.toTypedArray())

        applyAlbumArtworks(artworkDao.loadAlbums().associate { it.albumId!! to it.toArtwork() })

        return@withContext true
    }

    private fun ArtworkEntity.toArtwork(): Artwork {
        return when {
            !web.isNullOrBlank() -> Artwork.Web(web!!)
            !mediaStore.isNullOrBlank() -> Artwork.MediaStore(mediaStore!!.toUri())
            !internal.isNullOrBlank() -> Artwork.Internal(internal!!)
            else -> Artwork.Unknown
        }
    }

    private fun applyArtistArtworks(artworks: Map<Long, Artwork>) {
        _artistArtwork = artworks.toMutableMap()
    }

    private fun applyAlbumArtworks(artworks: Map<Long, Artwork>) {
        _albumArtwork = artworks.toMutableMap()
    }

    private fun getMediaStoreAlbumCoverUri(albumId: Long): Uri? {
        val sArtworkUri = "content://media/external/audio/albumart".toUri()
        val uri = ContentUris.withAppendedId(sArtworkUri, albumId)

        return if (isExistUri(uri)) uri else null
    }

    private fun isExistUri(uri: Uri): Boolean {
        return runCatching {
            context.contentResolver.openInputStream(uri)
        }.fold(
            onSuccess = { true },
            onFailure = { false },
        )
    }
}
