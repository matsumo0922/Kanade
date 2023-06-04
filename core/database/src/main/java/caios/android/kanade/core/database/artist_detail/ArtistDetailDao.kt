package caios.android.kanade.core.database.artist_detail

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface ArtistDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtistDetail(vararg entity: ArtistDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSimilarArtist(vararg entity: SimilarArtistEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertArtistTag(vararg entity: ArtistTagEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateArtistDetail(vararg entity: ArtistDetailEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateSimilarArtist(vararg entity: SimilarArtistEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateArtistTag(vararg entity: ArtistTagEntity)

    @Delete
    fun deleteArtistDetail(vararg entity: ArtistDetailEntity)

    @Delete
    fun deleteSimilarArtist(vararg entity: SimilarArtistEntity)

    @Delete
    fun deleteArtistTag(vararg entity: ArtistTagEntity)

    @Transaction
    @Query("DELETE FROM artist_detail WHERE artist_id = :artistId")
    fun delete(artistId: Long)

    @Transaction
    @Query("SELECT * FROM artist_detail")
    fun loadAll(): List<ArtistDetailModel>

    @Transaction
    @Query("SELECT * FROM artist_detail WHERE artist_id = :artistId")
    fun load(artistId: Long): ArtistDetailModel?
}