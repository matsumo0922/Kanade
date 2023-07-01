package caios.android.kanade.core.model.music

import android.net.Uri
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import java.util.Locale

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val artistId: Long,
    val album: String,
    val albumId: Long,
    val duration: Long,
    val year: Int,
    val track: Int,
    val mimeType: String,
    val data: String,
    val dateModified: Long,
    val uri: Uri,
    val artwork: Artwork,
) {
    val durationString: String
        get() {
            val second = duration / 1000
            val minute = second / 60
            val hour = minute / 60

            return if (hour > 0) {
                String.format(Locale.getDefault(), "%02d:%02d:%02d", hour, minute % 60, second % 60)
            } else {
                String.format(Locale.getDefault(), "%02d:%02d", minute, second % 60)
            }
        }

    companion object {
        fun dummy(id: Long = 0): Song {
            return Song(
                id = id,
                title = "サンプル楽曲$id",
                artist = "CAIOS",
                artistId = -1,
                album = "テストアルバム$id",
                albumId = -1,
                duration = 217392,
                year = -1,
                track = -1,
                mimeType = "audio/mpeg",
                data = "",
                dateModified = -1,
                uri = Uri.EMPTY,
                artwork = Artwork.Internal("Song"),
            )
        }

        fun dummies(count: Int): List<Song> {
            return (0 until count).map { dummy(it.toLong()) }
        }
    }
}

fun Song.toMediaItem(): MediaBrowserCompat.MediaItem {
    val metadata = getMetadataBuilder()
    return MediaBrowserCompat.MediaItem(metadata.build().description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE)
}

fun Song.getMetadataBuilder(): MediaMetadataCompat.Builder {
    return MediaMetadataCompat.Builder()
        .putText(MediaMetadataCompat.METADATA_KEY_TITLE, title)
        .putText(MediaMetadataCompat.METADATA_KEY_ARTIST, artist)
        .putText(MediaMetadataCompat.METADATA_KEY_ALBUM, album)
        .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, duration)
        .putLong(MediaMetadataCompat.METADATA_KEY_YEAR, year.toLong())
        .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, track.toLong())
        .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, id.toString())
}
