package caios.android.kanade.core.ui.view

import caios.android.kanade.core.model.music.Artwork

sealed interface CoordinatorData {
    data class Artist(
        val title: String,
        val summary: String,
        val artwork: Artwork,
    ) : CoordinatorData

    data class Album(
        val title: String,
        val summary: String,
        val artwork: Artwork,
    ) : CoordinatorData

    data class Playlist(
        val title: String,
        val summary: String,
        val artworks: List<Artwork>,
    ) : CoordinatorData
}
