package caios.android.kanade.core.repository

import caios.android.kanade.core.model.Artist
import caios.android.kanade.core.model.MusicConfig
import caios.android.kanade.core.model.Song

interface ArtistRepository {

    fun artist(artistId: Long, musicConfig: MusicConfig): Artist

    fun artists(musicConfig: MusicConfig): List<Artist>
    fun artists(query: String, musicConfig: MusicConfig): List<Artist>

    fun splitIntoArtists(songs: List<Song>, musicConfig: MusicConfig): List<Artist>
}
