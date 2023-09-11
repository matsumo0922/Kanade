package caios.android.kanade.core.music.upgrade

import android.content.Context
import caios.android.kanade.core.model.music.Playlist
import caios.android.kanade.core.model.music.PlaylistItem
import caios.android.kanade.core.repository.MusicRepository
import caios.android.kanade.core.repository.PlaylistRepository
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNames
import java.time.LocalDateTime

class OldPlaylistLoader(
    private val context: Context,
    private val musicRepository: MusicRepository,
    private val playlistRepository: PlaylistRepository,
) {
    fun hasOldItems(): Boolean {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val items = preference.all

        return items.isNotEmpty()
    }

    suspend fun upgrade() {
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val oldPlaylistMap = preference.all.toList().filterIsInstance<Pair<String, String>>().toMap()

        for ((name, json) in oldPlaylistMap) {
            val oldPlaylist = Json.decodeFromString(ListSerializer(OldPlaylist.serializer()), json)
            val playlist = Playlist(
                id = 0,
                name = name,
                items = oldPlaylist.mapNotNull {
                    musicRepository.getSong(it.musicId)?.let { it1 ->
                        PlaylistItem(
                            id = 0,
                            song = it1,
                            index = it.index,
                        )
                    }
                }.toSet(),
                createdAt = LocalDateTime.now(),
                isSystemPlaylist = (name == FAVORITE),
            )

            playlistRepository.create(playlist)
        }

        context.deleteSharedPreferences(PREFERENCE_NAME)
    }

    @Serializable
    private data class OldPlaylist(
        @JsonNames("a") val index: Int,
        @JsonNames("b") val musicId: Long,
    )

    companion object {
        const val PREFERENCE_NAME = "CAIOS-MusicPlaylist-2"
        const val FAVORITE = "<CAIOS-SYSTEM-FAVORITE>"
    }
}
