package project.android.footstamp.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StampDao {
    @Query("SELECT * FROM stamps")
    fun getAll(): LiveData<List<Stamp>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stamp: Stamp)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg stamps: Stamp)

    @Delete
    fun delete(stamp: Stamp)
}