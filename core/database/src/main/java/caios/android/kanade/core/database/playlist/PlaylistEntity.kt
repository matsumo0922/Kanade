package caios.android.kanade.core.database.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist",
    indices = [Index(value = ["id"], unique = true)],
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    val name: String,
    @ColumnInfo("is_system_playlist")
    val isSystemPlaylist: Boolean,
    @ColumnInfo("created_at")
    val createdAt: String? = null,
    @ColumnInfo("updated_at")
    val updatedAt: String? = null,
)
