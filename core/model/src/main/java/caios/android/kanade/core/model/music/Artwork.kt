package caios.android.kanade.core.model.music

import android.net.Uri

sealed class Artwork {
    data class MediaStore(val uri: Uri) : Artwork()
    data class Web(val url: String) : Artwork()
    data class Internal(val name: String) : Artwork()
    object Unknown : Artwork()
}
