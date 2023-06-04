package caios.android.kanade.core.database.album_detail

import androidx.room.Embedded
import androidx.room.Relation

class AlbumDetailModel {
    @Embedded
    lateinit var albumDetail: AlbumDetailEntity

    @Relation(
        parentColumn = "album_id",
        entityColumn = "album_id",
    )
    lateinit var tracks: List<AlbumTrackEntity>

    @Relation(
        parentColumn = "album_id",
        entityColumn = "album_id",
    )
    lateinit var tags: List<AlbumTagEntity>
}