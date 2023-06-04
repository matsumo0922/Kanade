package caios.android.kanade.core.database.artwork

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artwork",
    indices = [Index(value = ["id"], unique = true)],
)
data class ArtworkEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("artist_id")
    val artistId: Long? = null,
    @ColumnInfo("album_id")
    val albumId: Long? = null,
    @ColumnInfo("internal")
    val internal: String? = null,
    @ColumnInfo("media_store")
    val mediaStore: String? = null,
    @ColumnInfo("web")
    val web: String? = null,
)