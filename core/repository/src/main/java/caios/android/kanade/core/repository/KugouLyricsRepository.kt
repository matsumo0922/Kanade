package caios.android.kanade.core.repository

import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.LyricsPreference
import caios.android.kanade.core.model.entity.KugouLyricsEntity
import caios.android.kanade.core.model.entity.KugouSongEntity
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.parse
import caios.android.kanade.core.repository.util.parseLrc
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.util.decodeBase64String
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class KugouLyricsRepository @Inject constructor(
    private val client: HttpClient,
    private val lyricsPreference: LyricsPreference,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : LyricsRepository {

    private val cache = ConcurrentHashMap<Long, Lyrics>()
    private val _data = MutableStateFlow(emptyList<Lyrics>())
    private val scope = CoroutineScope(SupervisorJob() + io)

    init {
        scope.launch {
            lyricsPreference.data.collect { lyrics ->
                cache.clear()
                cache.putAll(lyrics.associateBy { it.songId })

                _data.value = lyrics.toList()
            }
        }
    }

    override val data: SharedFlow<List<Lyrics>> = _data.asSharedFlow()

    override suspend fun save(lyrics: Lyrics) {
        lyricsPreference.save(lyrics)
    }

    override fun get(song: Song): Lyrics? {
        return cache[song.id]
    }

    override suspend fun lyrics(song: Song): Lyrics? {
        return kotlin.runCatching {
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
        return client.get {
            url(SEARCH_ENDPOINT)
            parameter("ver", 1)
            parameter("client", "pc")
            parameter("man", "yes")
            parameter("hash", "")
            parameter("keyword", "$artist - $title")
            parameter("duration", "$duration")
        }.parse()
    }

    private suspend fun fetchSynchronizedLyrics(
        kugouSongId: String,
        accessKey: String,
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
