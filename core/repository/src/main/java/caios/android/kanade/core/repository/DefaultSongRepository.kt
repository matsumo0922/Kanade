package caios.android.kanade.core.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import android.provider.MediaStore.Audio.Media
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
import okhttp3.internal.toImmutableMap
import timber.log.Timber
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class DefaultSongRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val artworkRepository: ArtworkRepository,
) : SongRepository {

    private val cache = ConcurrentHashMap<Long, Song>()

    private val baseProjection = arrayOf(
        BaseColumns._ID,
        AudioColumns.TITLE,
        AudioColumns.TRACK,
        AudioColumns.YEAR,
        AudioColumns.DURATION,
        Media.DATA,
        AudioColumns.DATE_MODIFIED,
        AudioColumns.ALBUM_ID,
        AudioColumns.ALBUM,
        AudioColumns.ARTIST_ID,
        AudioColumns.ARTIST,
        AudioColumns.MIME_TYPE,
    )

    override fun get(songId: Long): Song? = cache[songId]

    override fun gets(songIds: List<Long>): List<Song> = songIds.mapNotNull { get(it) }

    override fun gets(): List<Song> = cache.values.toList()

    override suspend fun song(songId: Long, musicConfig: MusicConfig): Song? {
        return song(
            makeCursor(
                selection = AudioColumns._ID,
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

    override fun makeCursor(
        selection: String,
        selectionValues: List<String>,
        vararg musicOrders: MusicOrder,
    ): Cursor? {
        val order = musicOrders.joinToString(separator = ", ") { it.create() }
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else Media.EXTERNAL_CONTENT_URI

        var selectionFinal = AudioColumns.IS_MUSIC + "=1" + " AND " + AudioColumns.TITLE + " != ''"
        var selectionValuesFinal = emptyArray<String>()

        if (selection.isNotBlank()) {
            selectionFinal += " AND $selection"
            selectionValuesFinal += selectionValues
        }

        selectionFinal += " AND ${Media.DURATION} >= 5000"

        return try {
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

    override fun fetchArtwork() {
        var updates = 0

        for ((albumId, artwork) in artworkRepository.albumArtworks.toImmutableMap()) {
            for (song in cache.values.filter { it.albumId == albumId }) {
                if (song.artwork != artwork) updates++
                cache[song.id] = song.copy(artwork = artwork)
            }
        }

        Timber.d("Updated $updates songs artwork")
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
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Media.getContentUri(MediaStore.VOLUME_EXTERNAL) else Media.EXTERNAL_CONTENT_URI
        val albumArtworks = artworkRepository.albumArtworks.toImmutableMap()

        val id = cursor.getLong(AudioColumns._ID)
        val title = cursor.getString(AudioColumns.TITLE)
        val trackNumber = cursor.getInt(AudioColumns.TRACK)
        val year = cursor.getInt(AudioColumns.YEAR)
        val duration = cursor.getLong(AudioColumns.DURATION)
        val data = cursor.getString(AudioColumns.DATA)
        val dateModified = cursor.getLong(AudioColumns.DATE_MODIFIED)
        val albumId = cursor.getLong(AudioColumns.ALBUM_ID)
        val albumName = cursor.getStringOrNull(AudioColumns.ALBUM)
        val artistId = cursor.getLong(AudioColumns.ARTIST_ID)
        val artistName = cursor.getStringOrNull(AudioColumns.ARTIST)
        val mimeType = cursor.getString(AudioColumns.MIME_TYPE)

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
            artwork = albumArtworks[albumId] ?: Artwork.Unknown,
        ).also {
            cache[id] = it
        }
    }
}
