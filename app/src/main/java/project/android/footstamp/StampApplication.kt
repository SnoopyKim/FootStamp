package project.android.footstamp

import android.app.Application
import project.android.footstamp.model.StampDatabase
import project.android.footstamp.repository.StampRepository

class StampApplication : Application () {
    val database: StampDatabase by lazy { StampDatabase.getDatabase(this) }
    val repository: StampRepository by lazy { StampRepository(database.stampDao()) }
}
