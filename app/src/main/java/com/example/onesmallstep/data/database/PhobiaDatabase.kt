package com.example.onesmallstep.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.onesmallstep.data.entities.*
import com.example.onesmallstep.data.database.dao.*
import com.example.onesmallstep.utils.PhobiaDataSeeder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Phobia::class, ExposureLevel::class, ExposureStep::class, UserProgress::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class PhobiaDatabase : RoomDatabase() {

    abstract fun phobiaDao(): PhobiaDao
    abstract fun progressDao(): ProgressDao

    companion object {
        @Volatile
        private var INSTANCE: PhobiaDatabase? = null

        fun getDatabase(context: Context): PhobiaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PhobiaDatabase::class.java,
                    "phobia_database"
                )
                    .addCallback(PhobiaDatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class PhobiaDatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabase(database.phobiaDao())
                }
            }
        }

        suspend fun populateDatabase(phobiaDao: PhobiaDao) {
            // Insert sample data
            val phobias = PhobiaDataSeeder.getSamplePhobias()
            val levels = PhobiaDataSeeder.getSampleLevels()
            val steps = PhobiaDataSeeder.getSampleSteps()

            phobiaDao.insertPhobias(phobias)
            phobiaDao.insertLevels(levels)
            phobiaDao.insertSteps(steps)
        }
    }
}

// Type converter for Date
class Converters {
    @androidx.room.TypeConverter
    fun fromTimestamp(value: Long?): java.util.Date? {
        return value?.let { java.util.Date(it) }
    }

    @androidx.room.TypeConverter
    fun dateToTimestamp(date: java.util.Date?): Long? {
        return date?.time
    }
}