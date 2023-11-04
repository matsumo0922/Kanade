package caios.android.kanade.core.repository

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import caios.android.kanade.core.model.Order
import caios.android.kanade.core.model.music.Artwork
import caios.android.kanade.core.model.music.Song
import caios.android.kanade.core.model.player.MusicConfig
import caios.android.kanade.core.model.player.MusicOrder
import caios.android.kanade.core.model.player.MusicOrderOption
import caios.android.kanade.core.repository.util.getInt
import caios.android.kanade.core.repository.util.getLong
import caios.android.kanade.core.repository.util.getString
import caios.android.kanade.core.repository.util.getStringOrNull
import caios.android.kanade.core.repository.util.sortList
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import okhttp3.internal.toImmutableMap
import timber.log.Timber
import java.io.File
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

interface SongRepository {

    fun clear()

    fun get(songId: Long): Song?
    fun gets(songIds: List<Long>): List<Song>
    fun gets(): List<Song>

    suspend fun song(cursor: Cursor?): Song?
    suspend fun song(songId: Long, musicConfig: MusicConfig): Song?

    suspend fun songs(musicConfig: MusicConfig): List<Song>
    suspend fun songs(cursor: Cursor?): List<Song>

    suspend fun <T> useFile(song: Song, action: (File?) -> T): T
    suspend fun makeCursor(
        selection: String = "",
        selectionValues: List<String> = emptyList(),
        vararg musicOrders: MusicOrder = arrayOf(MusicOrder(Order.ASC, MusicOrderOption.Song.NAME)),
    ): Cursor?

    suspend fun delete(songIds: List<Long>)

    fun fetchArtwork()
    fun songsSort(songs: List<Song>, musicConfig: MusicConfig): List<Song>
}

class SongRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val artworkRepository: ArtworkRepository,
    private val userDataRepository: UserDataRepository,
) : SongRepository {

    private val cache = ConcurrentHashMap<Long, Song>()

    private val baseProjection = arrayOf(
        BaseColumns._ID,
        MediaStore.Audio.AudioColumns.TITLE,
        MediaStore.Audio.AudioColumns.TRACK,
        MediaStore.Audio.AudioColumns.YEAR,
        MediaStore.Audio.AudioColumns.DURATION,
        MediaStore.Audio.Media.DATA,
        MediaStore.Audio.AudioColumns.DATE_MODIFIED,
        MediaStore.Audio.AudioColumns.ALBUM_ID,
        MediaStore.Audio.AudioColumns.ALBUM,
        MediaStore.Audio.AudioColumns.ARTIST_ID,
        MediaStore.Audio.AudioColumns.ARTIST,
        MediaStore.Audio.AudioColumns.MIME_TYPE,
    )

    override fun clear() {
        cache.clear()
    }

    override fun get(songId: Long): Song? = cache[songId]

    override fun gets(songIds: List<Long>): List<Song> = songIds.mapNotNull { get(it) }

    override fun gets(): List<Song> = cache.values.toList()

    override suspend fun song(songId: Long, musicConfig: MusicConfig): Song? {
        return song(
            makeCursor(
                selection = MediaStore.Audio.AudioColumns._ID,
                selectionValues = listOf(songId.toString()),
                musicOrders = arrayOf(musicConfig.songOrder),
            ),
        )
    }

    override suspend fun song(cursor: Cursor?): Song? {
        val song = if (cursor != null && cursor.moveToFirst()) getSong(cursor) else null
        cursor?.close()
        return song
    }

    override suspend fun songs(musicConfig: MusicConfig): List<Song> {
        return songs(makeCursor(musicOrders = arrayOf(musicConfig.songOrder)))
    }

    override suspend fun songs(cursor: Cursor?): List<Song> {
        val songs = mutableListOf<Song>()
        if (cursor != null && cursor.moveToFirst()) {
            do {
                songs.add(getSong(cursor))
            } while (cursor.moveToNext())
        }
        cursor?.close()
        return songs
    }

    override suspend fun <T> useFile(song: Song, action: (File?) -> T): T {
        val file = kotlin.runCatching {
            val outputFileName = "${song.id}.${MimeTypeMap.getSingleton().getExtensionFromMimeType(song.mimeType)}"
            val outputFile = File(context.cacheDir, outputFileName)

            context.contentResolver.openInputStream(song.uri)?.use { inputStream ->
                inputStream.copyTo(outputFile.outputStream())
            }

            if (outputFile.exists()) outputFile else null
        }.getOrNull()

        val result = action.invoke(file)

        file?.delete()

        return result
    }

    override suspend fun makeCursor(
        selection: String,
        selectionValues: List<String>,
        vararg musicOrders: MusicOrder,
    ): Cursor? {
        return try {
            val order = musicOrders.joinToString(separator = ", ") { it.create() }
            val uri =
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            var selectionFinal = MediaStore.Audio.AudioColumns.TITLE + " != ''"
            var selectionValuesFinal = emptyArray<String>()

            if (selection.isNotBlank()) {
                selectionFinal += " AND $selection"
                selectionValuesFinal += selectionValues
            }

            if (userDataRepository.userData.first().isIgnoreNotMusic) {
                selectionFinal += " AND ${MediaStore.Audio.AudioColumns.IS_MUSIC}=1"
            }

            if (userDataRepository.userData.first().isIgnoreShortMusic) {
                selectionFinal += " AND ${MediaStore.Audio.Media.DURATION} >= 5000"
            }


            context.contentResolver.query(
                uri,
                baseProjection,
                selectionFinal,
                selectionValuesFinal,
                order,
            )
        } catch (ex: SecurityException) {
            Timber.w(ex, "Permission denied")
            return null
        }
    }

    override suspend fun delete(songIds: List<Long>) {
        val contentResolver = context.contentResolver

        for (uri in songIds.map { ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, it) }) {
            contentResolver.delete(uri, null, null)
        }
    }

    override fun fetchArtwork() {
        for ((albumId, artwork) in artworkRepository.albumArtworks.toImmutableMap()) {
            for (song in cache.values.filter { it.albumId == albumId }) {
                cache[song.id] = song.copy(albumArtwork = artwork)
            }
        }

        for ((artistId, artwork) in artworkRepository.artistArtworks.toImmutableMap()) {
            for (song in cache.values.filter { it.artistId == artistId }) {
                cache[song.id] = song.copy(artistArtwork = artwork)
            }
        }
    }

    override fun songsSort(songs: List<Song>, musicConfig: MusicConfig): List<Song> {
        val order = musicConfig.songOrder
        val option = order.option

        require(option is MusicOrderOption.Song) { "MusicOrderOption is not Song" }

        return when (option) {
            MusicOrderOption.Song.NAME -> songs.sortList({ it.title }, { it.artist }, order = order.order)
            MusicOrderOption.Song.ARTIST -> songs.sortList({ it.artist }, { it.title }, order = order.order)
            MusicOrderOption.Song.ALBUM -> songs.sortList({ it.album }, { it.title }, order = order.order)
            MusicOrderOption.Song.DURATION -> songs.sortList({ it.duration }, { it.title }, order = order.order)
            MusicOrderOption.Song.YEAR -> songs.sortList({ it.year }, { it.title }, order = order.order)
            MusicOrderOption.Song.TRACK -> songs.sortList({ it.track }, { it.title }, order = order.order)
        }
    }

    private fun getSong(cursor: Cursor): Song {
        val uri =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val albumArtworks = artworkRepository.albumArtworks.toImmutableMap()
        val artistArtworks = artworkRepository.artistArtworks.toImmutableMap()

        val id = cursor.getLong(MediaStore.Audio.AudioColumns._ID)
        val title = cursor.getString(MediaStore.Audio.AudioColumns.TITLE)
        val trackNumber = cursor.getInt(MediaStore.Audio.AudioColumns.TRACK)
        val year = cursor.getInt(MediaStore.Audio.AudioColumns.YEAR)
        val duration = cursor.getLong(MediaStore.Audio.AudioColumns.DURATION)
        val data = cursor.getString(MediaStore.Audio.AudioColumns.DATA)
        val dateModified = cursor.getLong(MediaStore.Audio.AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(MediaStore.Audio.AudioColumns.ALBUM_ID)
        val albumName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ALBUM)
        val artistId = cursor.getLong(MediaStore.Audio.AudioColumns.ARTIST_ID)
        val artistName = cursor.getStringOrNull(MediaStore.Audio.AudioColumns.ARTIST)
        val mimeType = cursor.getString(MediaStore.Audio.AudioColumns.MIME_TYPE)

        return Song(
            id = id,
            title = title,
            artist = artistName ?: "",
            artistId = artistId,
            album = albumName ?: "",
            albumId = albumId,
            duration = duration,
            year = year,
            track = trackNumber,
            mimeType = mimeType,
            data = data,
            dateModified = dateModified,
            uri = Uri.withAppendedPath(uri, id.toString()),
            albumArtwork = albumArtworks[albumId] ?: Artwork.Unknown,
            artistArtwork = artistArtworks[artistId] ?: Artwork.Unknown,
        ).also {
            cache[id] = it
        }
    }
}
