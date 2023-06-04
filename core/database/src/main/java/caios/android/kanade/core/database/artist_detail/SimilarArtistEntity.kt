package caios.android.kanade.core.database.artist_detail

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "similar_artist",
    indices = [Index(value = ["artist_id"])],
    foreignKeys = [ForeignKey(
        entity = ArtistDetailEntity::class,
        parentColumns = arrayOf("artist_id"),
        childColumns = arrayOf("artist_id"),
        onDelete = ForeignKey.CASCADE,
    )],
)
data class SimilarArtistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    @ColumnInfo(name = "artist_id") val artistId: Long,
    val name: String,
    val url: String,
    @ColumnInfo(name = "image_url") val imageUrl: String?,
)