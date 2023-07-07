package caios.android.kanade.core.database.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
interface PlayHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: PlayHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg entity: PlayHistoryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: PlayHistoryEntity)

    @Delete
    fun delete(entity: PlayHistoryEntity)

    @Transaction
    @Query("SELECT * FROM play_history")
    fun loadAll(): List<PlayHistoryEntity>
}
