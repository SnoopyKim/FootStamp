package project.android.footstamp.repository

import kotlinx.coroutines.flow.Flow
import project.android.footstamp.model.Stamp
import project.android.footstamp.model.StampDao

class StampRepository(private val stampDao: StampDao) {

    val allStamps: Flow<List<Stamp>> = stampDao.getStamps()

    fun getStamp(id: Int): Flow<Stamp> {
        return stampDao.getStamp(id)
    }

    suspend fun insert(stamp: Stamp) {
        stampDao.insert(stamp)
    }

    suspend fun insertAll(stamps: List<Stamp>) {
        stampDao.insertAll(*stamps.toTypedArray())
    }

    suspend fun update(stamp: Stamp) {
        stampDao.update(stamp)
    }

    suspend fun delete(stamp: Stamp) {
        stampDao.delete(stamp)
    }
}