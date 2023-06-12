package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.Song

interface AlbumRepository {

    fun get(albumId: Long): Album?
    fun gets(albumIds: List<Long>): List<Album>
    fun gets(): List<Album>

    suspend fun album(albumId: Long, musicConfig: MusicConfig): Album
    suspend fun albums(musicConfig: MusicConfig): List<Album>
    suspend fun albums(query: String, musicConfig: MusicConfig): List<Album>

    fun splitIntoAlbums(songs: List<Song>, musicConfig: MusicConfig): List<Album>
    fun applyArtwork(albumId: Long, artwork: Artwork)
    fun albumsSort(albums: List<Album>, musicConfig: MusicConfig): List<Album>
}
