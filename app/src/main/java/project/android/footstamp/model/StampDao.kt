package project.android.footstamp.model

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface StampDao {

    @Query("SELECT * FROM stamps WHERE id = :id")
    fun getStamp(id: Int): Flow<Stamp>

    @Query("SELECT * FROM stamps")
    fun getStamps(): Flow<List<Stamp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stamp: Stamp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg stamps: Stamp)

    @Update
    suspend fun update(stamp: Stamp)

    @Delete
    fun delete(stamp: Stamp)
}