package caios.android.kanade.navigation

import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import caios.android.kanade.core.design.R
import caios.android.kanade.core.design.icon.Icon
import caios.android.kanade.core.design.icon.Icon.DrawableResourceIcon
import caios.android.kanade.core.design.icon.KanadeIcon

enum class LibraryDestination(
    val icon: Icon,
    val textId: Int,
) {
    Home(
        icon = DrawableResourceIcon(KanadeIcon.asHome),
        textId = R.string.navigation_home,
    ),
    Playlist(
        icon = DrawableResourceIcon(KanadeIcon.asPlaylist),
        textId = R.string.navigation_playlist,
    ),
    Song(
        icon = DrawableResourceIcon(KanadeIcon.asSong),
        textId = R.string.navigation_song,
    ),
    Artist(
        icon = DrawableResourceIcon(KanadeIcon.asArtist),
        textId = R.string.navigation_artist,
    ),
    Album(
        icon = DrawableResourceIcon(KanadeIcon.asAlbum),
        textId = R.string.navigation_album,
    ),
}

fun NavDestination?.isLibraryDestinationInHierarchy(destination: LibraryDestination): Boolean {
    return this?.hierarchy?.any { it.route?.contains(destination.name, true) ?: false } == true
}
