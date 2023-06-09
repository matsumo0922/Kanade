package caios.android.kanade.core.repository

import android.content.ContentUris
import android.net.Uri
import androidx.core.net.toUri
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import timber.log.Timber
import javax.inject.Inject

class DefaultArtworkRepository @Inject constructor(
    private val artworkDao: ArtworkDao,
) : ArtworkRepository {

    private val _artistArtwork = mutableMapOf<Long, Artwork>()
    private val _albumArtwork = mutableMapOf<Long, Artwork>()

    override val artistArtworks: Map<Long, Artwork>
        get() = _artistArtwork.toMap()

    override val albumArtworks: Map<Long, Artwork>
        get() = _albumArtwork.toMap()

    override suspend fun artistArtworks(): Map<Long, Artwork> {
        return artworkDao.loadArtists().associate { entity ->
            entity.artistId!! to entity.toArtwork()
        }
    }

    override suspend fun albumArtworks(): Map<Long, Artwork> {
        return artworkDao.loadAlbums().associate { entity ->
            entity.albumId!! to entity.toArtwork()
        }
    }

    override suspend fun artistArtworks(artistIds: List<Long>): Map<Long, Artwork> {
        return artworkDao.loadArtists(artistIds).associate { entity ->
            entity.artistId!! to entity.toArtwork()
        }
    }

    override suspend fun albumArtworks(albumIds: List<Long>): Map<Long, Artwork> {
        return artworkDao.loadAlbums(albumIds).associate { entity ->
            entity.albumId!! to entity.toArtwork()
        }
    }

    override suspend fun artistArtwork(artistId: Long): Artwork {
        return artworkDao.loadArtist(artistId)?.toArtwork() ?: Artwork.Unknown
    }

    override suspend fun albumArtwork(albumId: Long): Artwork {
        return artworkDao.loadAlbum(albumId)?.toArtwork() ?: Artwork.Unknown
    }

    override suspend fun fetchArtistArtwork(artists: List<Artist>): Boolean {
        val registeredArtworks = artworkDao.loadArtists()
        val registeredIds = registeredArtworks.map { it.artistId }
        val uris = artists.filterNot { registeredIds.contains(it.artistId) }

        if (uris.isEmpty()) {
            Timber.d("Don't necessarily to fetch artist artwork. [fetched=${registeredIds.size}]")

            _artistArtwork.clear()
            _artistArtwork.putAll(registeredArtworks.associate { it.artistId!! to it.toArtwork() })
            return false
        }

        val entities = uris.map {
            ArtworkEntity(
                id = 0,
                artistId = it.artistId,
                internal = it.artist,
            )
        }

        artworkDao.insert(*entities.toTypedArray())

        _artistArtwork.clear()
        _artistArtwork.putAll(artworkDao.loadArtists().associate { it.artistId!! to it.toArtwork() })

        return true
    }

    override suspend fun fetchAlbumArtwork(albums: List<Album>): Boolean {
        val registeredIds = artworkDao.loadAlbums().map { it.albumId }
        val uris = albums
            .filterNot { registeredIds.contains(it.albumId) }
            .associateWith { getMediaStoreAlbumCoverUri(it.albumId) }

        if (uris.isEmpty()) {
            Timber.d("Don't necessarily to fetch album artwork. [fetched=${registeredIds.size}]")

            _albumArtwork.clear()
            _albumArtwork.putAll(artworkDao.loadAlbums().associate { it.albumId!! to it.toArtwork() })
            return false
        }

        val entities = uris.map {
            ArtworkEntity(
                id = 0,
                albumId = it.key.albumId,
                mediaStore = it.value.toString(),
                internal = it.key.album,
            )
        }

        artworkDao.insert(*entities.toTypedArray())

        _albumArtwork.clear()
        _albumArtwork.putAll(artworkDao.loadAlbums().associate { it.albumId!! to it.toArtwork() })

        return true
    }

    private fun ArtworkEntity.toArtwork(): Artwork {
        return when {
            !web.isNullOrBlank() -> Artwork.Web(web!!)
            !mediaStore.isNullOrBlank() -> Artwork.MediaStore(mediaStore!!.toUri())
            !internal.isNullOrBlank() -> Artwork.Internal(internal!!)
            else -> Artwork.Unknown
        }
    }

    private fun getMediaStoreAlbumCoverUri(albumId: Long): Uri {
        val sArtworkUri = "content://media/external/audio/albumart".toUri()
        return ContentUris.withAppendedId(sArtworkUri, albumId)
    }
}
