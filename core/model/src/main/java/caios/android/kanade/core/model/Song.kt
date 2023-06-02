package caios.android.kanade.core.model

import android.net.Uri

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val artistId: Long,
    val album: String,
    val albumId: Long,
    val duration: Long,
    val year: Long,
    val track: Long,
    val disc: Long,
    val mimeType: String,
    val path: String,
    val uri: Uri,
)
