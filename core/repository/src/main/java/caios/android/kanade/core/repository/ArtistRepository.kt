package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.Song

interface ArtistRepository {

    suspend fun artist(artistId: Long, musicConfig: MusicConfig): Artist

    suspend fun artists(musicConfig: MusicConfig): List<Artist>
    suspend fun artists(query: String, musicConfig: MusicConfig): List<Artist>

    fun splitIntoArtists(songs: List<Song>, musicConfig: MusicConfig): List<Artist>
}
