package caios.android.kanade.core.model.music

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class Artwork: Parcelable {
    data class MediaStore(val uri: Uri) : Artwork()
    data class Web(val url: String) : Artwork()
    data class Internal(val name: String) : Artwork()
    object Unknown : Artwork()
}