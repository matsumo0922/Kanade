package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import kotlinx.coroutines.flow.SharedFlow

interface LyricsRepository {
    val data: SharedFlow<List<Lyrics>>

    fun get(song: Song): Lyrics?

    suspend fun save(lyrics: Lyrics)
    suspend fun lyrics(song: Song): Lyrics?
}
