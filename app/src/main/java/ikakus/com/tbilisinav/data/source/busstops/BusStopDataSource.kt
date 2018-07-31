package ikakus.com.tbilisinav.data.source.busstops

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import io.reactivex.Single

interface BusStopDataSource {
    fun getAllStops(): Single<List<MapBusStop>>

    fun getAllStopsInBounds(bounds: LatLngBounds): Single<List<MapBusStop>>

    fun getNearby(location: LatLng): Single<MutableList<MapBusStop>>

    fun getBusStopInfo(id: Int): Single<BusStopInfoModel>

}