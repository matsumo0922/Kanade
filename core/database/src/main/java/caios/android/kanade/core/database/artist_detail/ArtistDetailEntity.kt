package caios.android.kanade.core.database.artist_detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "artist_detail",
    indices = [Index(value = ["artist_id"], unique = true)],
)
data class ArtistDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "artist_id")
    val artistId: Long,
    @ColumnInfo(name = "name")
    val artistName: String,
    val mbid: String?,
    val url: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
    val biography: String?,
)