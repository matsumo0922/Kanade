package caios.android.kanade.db.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_item",
    indices = [Index(value = ["playlist_id"])],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlist_id"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class PlaylistItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("playlist_id")
    val playlistId: Long,
    val index: Int,
    @ColumnInfo("song_id")
    val songId: Long,
)
