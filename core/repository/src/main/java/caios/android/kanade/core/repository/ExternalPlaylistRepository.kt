package caios.android.kanade.core.repository

import caios.android.kanade.core.model.music.ExternalPlaylist
import caios.android.kanade.core.model.music.Playlist

interface ExternalPlaylistRepository {

    suspend fun getExternalPlaylists(): List<ExternalPlaylist>

    suspend fun export(playlist: Playlist)
    suspend fun import(externalPlaylistId: Long)
}
