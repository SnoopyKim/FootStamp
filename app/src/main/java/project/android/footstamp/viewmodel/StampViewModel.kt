package project.android.footstamp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import project.android.footstamp.model.AppDatabase
import project.android.footstamp.model.Stamp

class StampViewModel(application: Application) : AndroidViewModel(application){
    private val db = AppDatabase.getInstance(application)

    fun getAll(): LiveData<List<Stamp>> {
        return db!!.stampDao().getAll()
    }

    fun insert(stamp: Stamp) {
        db!!.stampDao().insert(stamp)
    }

    fun insertAll(vararg stamps: Stamp) {
        db!!.stampDao().insertAll(*stamps)
    }

    fun delete(stamp: Stamp) {
        db!!.stampDao().delete(stamp)
    }

}