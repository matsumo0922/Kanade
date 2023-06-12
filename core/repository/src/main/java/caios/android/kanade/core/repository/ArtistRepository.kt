package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Artist
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.MusicConfig
import caios.android.kanade.core.model.music.Song

interface ArtistRepository {

    fun get(artistId: Long): Artist?
    fun gets(artistIds: List<Long>): List<Artist>
    fun gets(): List<Artist>

    suspend fun artist(artistId: Long, musicConfig: MusicConfig): Artist

    suspend fun artists(musicConfig: MusicConfig): List<Artist>
    suspend fun artists(query: String, musicConfig: MusicConfig): List<Artist>

    fun splitIntoArtists(songs: List<Song>, musicConfig: MusicConfig): List<Artist>
    fun applyArtwork(artistId: Long, artwork: Artwork)
    fun artistsSort(artists: List<Artist>, musicConfig: MusicConfig): List<Artist>
}
