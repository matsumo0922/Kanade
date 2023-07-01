package caios.android.kanade.core.database.playlist

import androidx.room.Embedded
import androidx.room.Relation

class PlaylistModel {
    @Embedded
    lateinit var playlist: PlaylistEntity

    @Relation(
        parentColumn = "id",
        entityColumn = "playlist_id",
    )
    lateinit var items: List<PlaylistItemEntity>
}
