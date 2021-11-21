package project.android.footstamp.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.Flow
import project.android.footstamp.model.Stamp
import project.android.footstamp.model.StampDao
class StampRepository(private val stampDao: StampDao) {
    val myRef = FirebaseDatabase.getInstance().getReference("uid")

    val getAllFromFirebase = FirebaseAuth.getInstance().currentUser?.let {
        myRef.child(it.uid).get()
    }
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

