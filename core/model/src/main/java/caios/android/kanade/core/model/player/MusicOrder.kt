package caios.android.kanade.core.model.player

import android.provider.MediaStore
import caios.android.kanade.core.model.Order

data class MusicOrder(
    val order: Order,
    val option: MusicOrderOption,
) {
    fun create(): String {
        val option = when (option) {
            is MusicOrderOption.Artist -> option.option
            is MusicOrderOption.Album -> option.option
            is MusicOrderOption.Song -> option.option
            is MusicOrderOption.Playlist -> option.option
        }

        return if (order == Order.ASC) option else "$option DESC"
    }

    companion object {
        fun songDefault(): MusicOrder {
            return MusicOrder(
                order = Order.ASC,
                option = MusicOrderOption.Song.NAME,
            )
        }

        fun artistDefault(): MusicOrder {
            return MusicOrder(
                order = Order.ASC,
                option = MusicOrderOption.Artist.NAME,
            )
        }

        fun albumDefault(): MusicOrder {
            return MusicOrder(
                order = Order.ASC,
                option = MusicOrderOption.Album.NAME,
            )
        }

        fun playlistDefault(): MusicOrder {
            return MusicOrder(
                order = Order.ASC,
                option = MusicOrderOption.Playlist.NAME,
            )
        }
    }
}

sealed interface MusicOrderOption {

    enum class Artist(val option: String) : MusicOrderOption {
        NAME(MediaStore.Audio.Artists.DEFAULT_SORT_ORDER),
        TRACKS(MediaStore.Audio.Artists.NUMBER_OF_TRACKS),
        ALBUMS(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS),
    }

    enum class Album(val option: String) : MusicOrderOption {
        NAME(MediaStore.Audio.Albums.DEFAULT_SORT_ORDER),
        TRACKS(MediaStore.Audio.Albums.NUMBER_OF_SONGS),
        ARTIST(MediaStore.Audio.Media.ARTIST),
        YEAR(MediaStore.Audio.Media.YEAR),
    }

    enum class Song(val option: String) : MusicOrderOption {
        NAME(MediaStore.Audio.Media.DEFAULT_SORT_ORDER),
        ARTIST(MediaStore.Audio.Artists.DEFAULT_SORT_ORDER),
        ALBUM(MediaStore.Audio.Albums.DEFAULT_SORT_ORDER),
        DURATION(MediaStore.Audio.Media.DURATION),
        YEAR(MediaStore.Audio.Media.YEAR),
        TRACK(MediaStore.Audio.Media.TRACK),
    }

    enum class Playlist(val option: String) : MusicOrderOption {
        NAME(MediaStore.Audio.Playlists.DEFAULT_SORT_ORDER),
        TRACKS(MediaStore.Audio.Playlists.NUM_TRACKS),
    }
}
