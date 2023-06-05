package caios.android.kanade.core.model.music

import android.graphics.Bitmap
import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import java.io.ByteArrayOutputStream

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
)

fun Song.toMediaItem(bitmap: Bitmap?): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(title)
        .setArtist(artist)
        .setAlbumTitle(album)

    if (bitmap != null) {
        val stream = ByteArrayOutputStream().also { bitmap.compress(Bitmap.CompressFormat.PNG, 90, it) }
        val array = stream.toByteArray()

        metadata.maybeSetArtworkData(array, MediaMetadata.PICTURE_TYPE_FRONT_COVER)
    } else {
        metadata.setArtworkUri(null)
    }

    return MediaItem.Builder()
        .setUri(uri)
        .setMediaMetadata(metadata.build())
        .build()
}
