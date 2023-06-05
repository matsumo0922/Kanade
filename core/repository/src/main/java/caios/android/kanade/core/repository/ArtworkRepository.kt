package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Artwork

interface ArtworkRepository {

    suspend fun artistArtwork(artistId: Long): Artwork
    suspend fun albumArtwork(albumId: Long): Artwork

    suspend fun artistArtworks(artistIds: List<Long>): Map<Long, Artwork>
    suspend fun albumArtworks(albumIds: List<Long>): Map<Long, Artwork>

    suspend fun artistArtworks(): Map<Long, Artwork>
    suspend fun albumArtworks(): Map<Long, Artwork>
}