package caios.android.kanade.core.repository

import caios.android.kanade.core.datastore.LyricsPreference
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.parse
import caios.android.kanade.core.repository.util.parseLrc
import caios.android.kanade.music.lyrics.entity.KugouLyricsEntity
import caios.android.kanade.music.lyrics.entity.KugouSongEntity
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.util.decodeBase64String
import timber.log.Timber
import javax.inject.Inject

class DefaultLyricsRepository @Inject constructor(
    private val client: HttpClient,
    private val lyricsPreference: LyricsPreference,
): LyricsRepository {

    override fun get(song: Song): Lyrics? {
        return lyricsPreference.data.find { it.songId == song.id }
    }

    override suspend fun lyrics(song: Song): Lyrics? {
        return lyricsPreference.data.find { it.songId == song.id } ?: kotlin.runCatching {
            Timber.d("Fetch lyrics: ${song.title} - ${song.artist}")

            val songData = fetchSong(song.title, song.artist, song.duration)
            val candidate = songData?.candidates?.firstOrNull() ?: return@runCatching null
            val lyricsData = fetchSynchronizedLyrics(songData.proposal, candidate.accessKey) ?: return@runCatching null
            val lyrics = lyricsData.content.decodeBase64String()

            parseLrc(song, lyrics)
        }.getOrNull()?.also {
            lyricsPreference.save(it)
        }
    }

    private suspend fun fetchSong(
        title: String,
        artist: String,
        duration: Long,
    ): KugouSongEntity? {
        try {
            return client.get {
                url(SEARCH_ENDPOINT)
                parameter("ver", 1)
                parameter("client", "pc")
                parameter("man", "yes")
                parameter("hash", "")
                parameter("keyword", "$artist - $title")
                parameter("duration", "$duration")
            }.parse()
        } catch (e: Throwable) {
            Timber.e(e)
            return null
        }
    }

    private suspend fun fetchSynchronizedLyrics(
        kugouSongId: String,
        accessKey: String
    ): KugouLyricsEntity? {
        return client.get {
            url(DOWNLOAD_ENDPOINT)
            parameter("ver", 1)
            parameter("client", "pc")
            parameter("fmt", "lrc")
            parameter("charset", "utf8")
            parameter("id", kugouSongId)
            parameter("accesskey", accessKey)
        }.parse()
    }

    companion object {
        private const val SEARCH_ENDPOINT = "http://lyrics.kugou.com/search"
        private const val DOWNLOAD_ENDPOINT = "http://lyrics.kugou.com/download"
    }
}