package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Album
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.MusicOrder
import caios.android.kanade.core.model.music.Song

interface AlbumRepository {

    suspend fun album(albumId: Long, musicConfig: MusicConfig): Album

    suspend fun albums(musicConfig: MusicConfig): List<Album>
    suspend fun albums(query: String, musicConfig: MusicConfig): List<Album>

    fun splitIntoAlbums(songs: List<Song>, musicOrder: MusicOrder): List<Album>
}
