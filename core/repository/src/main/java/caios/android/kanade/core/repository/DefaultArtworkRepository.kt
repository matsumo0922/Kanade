package caios.android.kanade.core.repository

import androidx.core.net.toUri
import caios.android.kanade.core.database.artwork.ArtworkDao
import caios.android.kanade.core.database.artwork.ArtworkEntity
import caios.android.kanade.core.model.music.Artwork
import javax.inject.Inject

class DefaultArtworkRepository @Inject constructor(
    private val artworkDao: ArtworkDao,
) : ArtworkRepository {

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

    private fun ArtworkEntity.toArtwork(): Artwork {
        return when {
            !web.isNullOrBlank() -> Artwork.Web(web!!)
            !mediaStore.isNullOrBlank() -> Artwork.MediaStore(mediaStore!!.toUri())
            !internal.isNullOrBlank() -> Artwork.Internal(internal!!)
            else -> Artwork.Unknown
        }
    }
}
