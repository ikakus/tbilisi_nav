package ikakus.com.tbilisinav.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import ikakus.com.tbilisinav.data.database.models.MapBusStop


@Database(entities = arrayOf(MapBusStop::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun busStopDao(): BusStopDao
}