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
    val year: Int,
    val track: Int,
    val mimeType: String,
    val data: String,
    val dateModified: Long,
    val uri: Uri,
)
