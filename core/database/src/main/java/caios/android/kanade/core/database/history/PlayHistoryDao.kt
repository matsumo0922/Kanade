package caios.android.kanade.core.database.history

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update

@Dao
abstract class PlayHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: PlayHistoryEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(vararg entity: PlayHistoryEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun update(entity: PlayHistoryEntity)

    @Delete
    abstract fun delete(entity: PlayHistoryEntity)

    @Transaction
    @Query("SELECT * FROM play_history")
    abstract fun loadAll(): List<PlayHistoryEntity>
}
