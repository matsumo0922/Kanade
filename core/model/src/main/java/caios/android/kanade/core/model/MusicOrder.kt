package caios.android.kanade.core.model

import android.provider.MediaStore

data class MusicOrder(
    val order: Order,
    val option: String,
) {
    fun create(): String {
        return if (order == Order.ASC) {
            option
        } else {
            "$option DESC"
        }
    }
}

object MusicOrderOption {
    object Artist {
        const val NAME = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
        const val TRACKS = MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        const val ALBUMS = MediaStore.Audio.Artists.NUMBER_OF_ALBUMS
    }

    object Album {
        const val NAME = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        const val TRACKS = MediaStore.Audio.Albums.NUMBER_OF_SONGS
        const val ARTIST = MediaStore.Audio.Media.ARTIST
        const val YEAR = MediaStore.Audio.Media.YEAR
    }

    object Song {
        const val NAME = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        const val ARTIST = MediaStore.Audio.Artists.DEFAULT_SORT_ORDER
        const val ALBUM = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        const val DURATION = MediaStore.Audio.Media.DURATION
        const val YEAR = MediaStore.Audio.Media.YEAR
    }
}