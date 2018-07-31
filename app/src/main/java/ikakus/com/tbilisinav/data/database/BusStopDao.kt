package ikakus.com.tbilisinav.data.database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import io.reactivex.Single

@Dao
interface BusStopDao {
    @Query("SELECT * FROM MapBusStop")
    fun getAll(): Single<List<MapBusStop>>

    @Query("SELECT * FROM MapBusStop WHERE lat BETWEEN :swLat AND :neLat AND lng BETWEEN :swLng AND :neLng")
    fun getAllInbounds(swLat: Double, swLng: Double, neLat: Double, neLng: Double):
            Single<List<MapBusStop>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    fun insertAll(list: List<MapBusStop>)
}