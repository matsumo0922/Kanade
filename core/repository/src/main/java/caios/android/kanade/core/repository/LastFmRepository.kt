package caios.android.kanade.core.repository

import Ignores
import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.AlbumDetail
import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.ArtistDetail
import kotlinx.coroutines.flow.SharedFlow

interface LastFmRepository {
    val artistDetails: SharedFlow<Map<Long, ArtistDetail>>
    val albumDetails: SharedFlow<Map<Long, AlbumDetail>>

    fun getArtistDetails(): Map<Long, ArtistDetail>
    fun getAlbumDetails(): Map<Long, AlbumDetail>

    suspend fun artistDetail(artist: Artist): ArtistDetail?
    suspend fun albumDetail(album: Album): AlbumDetail?

    suspend fun fetchArtistDetails()
    suspend fun fetchAlbumDetails()

    fun setIgnoreArtist(artist: Artist)
    fun setIgnoreAlbum(album: Album)

    fun getIgnores(): Ignores
}
