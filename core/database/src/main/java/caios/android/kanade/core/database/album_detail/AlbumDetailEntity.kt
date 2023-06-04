package caios.android.kanade.core.database.album_detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "album_detail",
    indices = [Index(value = ["album_id"], unique = true)],
)
data class AlbumDetailEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "album_id")
    val albumId: Long,
    @ColumnInfo(name = "artist_name")
    val artistName: String,
    @ColumnInfo(name = "album_name")
    val albumName: String,
    val mbid: String?,
    @ColumnInfo(name = "image_url")
    val imageUrl: String?,
)