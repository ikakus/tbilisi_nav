package ikakus.com.tbilisinav.data.source.busstops

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import io.reactivex.Single

class BusStopRepository(
        private val remoteDataSource: BusStopDataSource,
        private val localDataSource: BusStopDataSource)
    : BusStopDataSource {
    override fun getAllStopsInBounds(bounds: LatLngBounds): Single<List<MapBusStop>> {
        return localDataSource.getAllStopsInBounds(bounds)
    }

    override fun getNearby(location: LatLng): Single<MutableList<MapBusStop>> {
       return localDataSource.getNearby(location)
    }

    override fun getAllStops(): Single<List<MapBusStop>> {
        return localDataSource.getAllStops()
    }

    override fun getBusStopInfo(id: Int): Single<BusStopInfoModel> {
        return remoteDataSource.getBusStopInfo(id)
    }
}