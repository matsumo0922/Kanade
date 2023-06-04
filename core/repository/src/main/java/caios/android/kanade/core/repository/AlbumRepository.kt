package caios.android.kanade.core.repository

import caios.android.kanade.core.model.Album
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.Song

interface AlbumRepository {

    fun album(albumId: Long, musicConfig: MusicConfig): Album

    fun albums(musicConfig: MusicConfig): List<Album>
    fun albums(query: String, musicConfig: MusicConfig): List<Album>

    fun splitIntoAlbums(songs: List<Song>, musicOrder: MusicOrder): List<Album>
}
