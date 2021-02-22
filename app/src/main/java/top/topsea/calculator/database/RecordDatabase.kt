package top.topsea.calculator.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private var instance: RecordDatabase? = null
@Database(entities = [Record::class], version = 1)
abstract class RecordDatabase : RoomDatabase() {

    companion object {
        fun getInstance(context: Context): RecordDatabase? {
            if (instance == null) {
                synchronized(RecordDatabase::class.java) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            RecordDatabase::class.java, "Records"
                        )
                            .allowMainThreadQueries().build()
                    }
                }
            }
            return instance
        }

    }
    abstract fun recordDao(): RecordDao
}