package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig

interface ArtistRepository {

    fun clear()

    fun get(artistId: Long): Artist?
    fun gets(artistIds: List<Long>): List<Artist>
    fun gets(): List<Artist>

    suspend fun artist(artistId: Long, musicConfig: MusicConfig): Artist

    suspend fun artists(musicConfig: MusicConfig): List<Artist>
    suspend fun artists(query: String, musicConfig: MusicConfig): List<Artist>

    fun splitIntoArtists(songs: List<Song>, musicConfig: MusicConfig): List<Artist>
    fun fetchArtwork()
    fun artistsSort(artists: List<Artist>, musicConfig: MusicConfig): List<Artist>
}
