package caios.android.kanade.core.database.history

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "play_history",
    indices = [Index(value = ["id"], unique = true)],
)
data class PlayHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long,
    @ColumnInfo("song_id")
    val songId: Long,
    @ColumnInfo("duration")
    val duration: Long,
    @ColumnInfo("created_at")
    val createdAt: String,
)
