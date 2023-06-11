package caios.android.kanade.core.model.music

import android.net.Uri
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

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
    companion object {
        fun dummy(id: String = ""): Song {
            return Song(
                id = -1,
                title = "サンプル楽曲$id",
                artist = "CAIOS",
                artistId = -1,
                album = "テストアルバム",
                albumId = -1,
                duration = -1,
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
            return (0 until count).map { dummy(it.toString()) }
        }
    }
}

fun Song.toMediaItem(): MediaItem {
    val metadata = MediaMetadata.Builder()
        .setTitle(title)
        .setArtist(artist)
        .setAlbumTitle(album)
        .setExtras(Bundle().apply { putParcelable("artwork", artwork) })

    return MediaItem.Builder()
        .setMediaId(id.toString())
        .setUri(uri)
        .setMediaMetadata(metadata.build())
        .build()
}
