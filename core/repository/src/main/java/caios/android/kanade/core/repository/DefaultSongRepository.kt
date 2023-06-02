package caios.android.kanade.core.repository

import android.content.Context
import android.database.Cursor
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import caios.android.kanade.core.model.MusicOrder
import caios.android.kanade.core.model.Song
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultSongRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) : SongRepository {

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
    )

    override fun song(songId: Long): Song {

    }

    override fun song(cursor: Cursor?): Song {

    }

    override fun songs(): List<Song> {

    }

    override fun songs(cursor: Cursor): List<Song> {

    }

    override fun makeCursor(
        selection: String,
        selectionValues: List<String>,
        musicOrder: MusicOrder,
    ): Cursor? {
        val order = musicOrder.create()
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val isMusic = MediaStore.Audio.AudioColumns.IS_MUSIC + "=1" + " AND " + MediaStore.Audio.AudioColumns.TITLE + " != ''"
        val finalSelection = "$isMusic $selection"

        return try {
            context.contentResolver.query(
                uri,
                baseProjection,
                selectionFinal,
                selectionValuesFinal,
                sortOrder
            )
        } catch (ex: SecurityException) {
            return null
        }
    }
}