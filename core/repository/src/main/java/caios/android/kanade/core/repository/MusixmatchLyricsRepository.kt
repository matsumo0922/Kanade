package caios.android.kanade.core.repository

import caios.android.kanade.core.common.network.Dispatcher
import caios.android.kanade.core.common.network.KanadeConfig
import caios.android.kanade.core.common.network.KanadeDispatcher
import caios.android.kanade.core.datastore.LyricsPreference
import caios.android.kanade.core.datastore.TokenPreference
import caios.android.kanade.core.model.entity.MusixmatchLyricsEntity
import caios.android.kanade.core.model.entity.MusixmatchSongsEntity
import caios.android.kanade.core.model.music.Lyrics
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.repository.util.parseLrc
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class MusixmatchLyricsRepository @Inject constructor(
    private val client: HttpClient,
    private val lyricsPreference: LyricsPreference,
    private val tokenPreference: TokenPreference,
    private val kanadeConfig: KanadeConfig,
    @Dispatcher(KanadeDispatcher.IO) private val io: CoroutineDispatcher,
) : LyricsRepository {

    private val formatter = Json { ignoreUnknownKeys = true }
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
            val token = tokenPreference.get(TokenPreference.KEY_MUSIXMATCH) ?: if (kanadeConfig.isDebug) kanadeConfig.musixmatchApiKey else return@runCatching null
            val songs = fetchSongs(token, song.title, song.artist) ?: return@runCatching null
            val track = findTrack(songs.message.body.trackList, (song.duration / 1000).toInt()) ?: return@runCatching null
            val entity = fetchLyrics(token, track.track.trackId) ?: return@runCatching null

            Timber.d("RegisterLyrics:  ${track.track.trackName} - ${track.track.artistName}")

            parseLrc(song, entity.message.body.subtitle.subtitleBody)
        }.fold(
            onSuccess = { lyrics -> lyrics?.also { lyricsPreference.save(it) } },
            onFailure = {
                Timber.w(it)
                null
            },
        )
    }

    private suspend fun fetchSongs(userToken: String, title: String, artist: String): MusixmatchSongsEntity? {
        val serializer = MusixmatchSongsEntity.serializer()
        val body = client.get {
            url(SEARCH_ENDPOINT)
            parameter("q_track", title)
            parameter("q_artist", artist)
            parameter("format", "json")
            parameter("page_size", 10)
            parameter("page", 1)
            parameter("s_track_rating", "desc")
            parameter("f_has_lyrics", true)
            parameter("quorum_factor", 1.0)
            parameter("app_id", "web-desktop-app-v1.0")
            parameter("usertoken", userToken)
        }.bodyAsText()

        return formatter.decodeFromString(serializer, body)
    }

    private suspend fun fetchLyrics(userToken: String, trackId: Long): MusixmatchLyricsEntity? {
        val serializer = MusixmatchLyricsEntity.serializer()
        val body = client.get {
            url(LYRICS_ENDPOINT)
            parameter("format", "json")
            parameter("track_id", trackId)
            parameter("subtitle_format", "lrc")
            parameter("app_id", "web-desktop-app-v1.0")
            parameter("usertoken", userToken)
        }.bodyAsText()

        return formatter.decodeFromString(serializer, body)
    }

    private fun findTrack(tracks: List<MusixmatchSongsEntity.Message.Body.Track>, duration: Int): MusixmatchSongsEntity.Message.Body.Track? {
        return tracks.minByOrNull {
            Timber.d("SuggestTrack: ${it.track.trackName} - ${it.track.artistName}")
            kotlin.math.abs(it.track.trackLength - duration)
        }
    }

    companion object {
        private const val SEARCH_ENDPOINT = "https://apic-desktop.musixmatch.com/ws/1.1/track.search"
        private const val LYRICS_ENDPOINT = "https://apic-desktop.musixmatch.com/ws/1.1/track.subtitle.get"
    }
}
