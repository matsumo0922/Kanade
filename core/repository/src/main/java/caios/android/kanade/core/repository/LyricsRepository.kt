package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song

interface LyricsRepository {
    fun get(song: Song): Lyrics?
    suspend fun lyrics(song: Song): Lyrics?
}
