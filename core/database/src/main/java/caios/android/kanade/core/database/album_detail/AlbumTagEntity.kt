package caios.android.kanade.core.database.album_detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_tag",
    indices = [Index(value = ["album_id"])],
    foreignKeys = [
        ForeignKey(
            entity = AlbumDetailEntity::class,
            parentColumns = ["album_id"],
            childColumns = ["album_id"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
)
data class AlbumTagEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo(name = "album_id")
    val albumId: Long,
    val name: String,
    val url: String,
)
