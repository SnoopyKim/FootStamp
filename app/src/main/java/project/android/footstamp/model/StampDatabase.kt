package project.android.footstamp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Stamp::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class StampDatabase : RoomDatabase() {
    abstract fun stampDao(): StampDao

    companion object {
        @Volatile
        private var INSTANCE: StampDatabase? = null

        fun getDatabase(context: Context): StampDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StampDatabase::class.java,
                    "stamp.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}