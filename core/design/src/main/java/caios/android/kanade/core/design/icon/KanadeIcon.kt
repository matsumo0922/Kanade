package caios.android.kanade.core.design.icon

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.vector.ImageVector
import caios.android.kanade.core.design.R

object KanadeIcon {
    // animated-selector drawable
    val asHome = R.drawable.as_home
    val asPlaylist = R.drawable.as_playlist
    val asSong = R.drawable.as_songs
    val asArtist = R.drawable.as_artist
    val asAlbum = R.drawable.as_album
}

sealed class Icon {
    data class ImageVectorIcon(val imageVector: ImageVector) : Icon()
    data class DrawableResourceIcon(@DrawableRes val id: Int) : Icon()
}
