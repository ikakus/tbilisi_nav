package ikakus.com.tbilisinav.data.source.busstops.local

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.database.BusStopDao
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.data.source.busstops.BusStopDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import io.reactivex.Observable
import io.reactivex.Single
import nl.tastyclub.android.tastyclub.utils.distancecalculator.DistanceCalculatorImpl
import nl.tastyclub.android.tastyclub.utils.distancecalculator.IDistanceCalculator
import timber.log.Timber

class LocalBusStopDataSource(private val busDao: BusStopDao) : BusStopDataSource {

    val RADIUS = 250.0

    override fun getAllStopsInBounds(bounds: LatLngBounds): Single<List<MapBusStop>> {
        return busDao.getAllInbounds(
                bounds.southwest.latitude,
                bounds.southwest.longitude,
                bounds.northeast.latitude,
                bounds.northeast.longitude)
    }

    override fun getNearby(location: LatLng): Single<MutableList<MapBusStop>> {
        val allStopsObs = getAllStops()
        return allStopsObs.subscribeOn(ImmediateSchedulerProvider().io())
                .flatMap { it -> getStopsInRadius(it, location, RADIUS) }
    }

    override fun getAllStops(): Single<List<MapBusStop>> {
        return busDao.getAll()
    }

    override fun getBusStopInfo(id: Int): Single<BusStopInfoModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getStopsInRadius(
            allStops: List<MapBusStop>,
            location: LatLng,
            radius: Double)
            : Single<MutableList<MapBusStop>> {

        val distanceCalculator = DistanceCalculatorImpl()
        val sortedByDistance =
                sortByDistance(distanceCalculator, allStops, location)

        return Observable.fromIterable(sortedByDistance).filter {
            distanceCalculator.getDistance(
                    location,
                    LatLng(it.lat, it.lng)) <= radius
        }.toList()

    }

    private fun sortByDistance(distanceCalculator: IDistanceCalculator,
                               stops: List<MapBusStop>,
                               location: LatLng): List<MapBusStop>? {
        try {
            return stops.sortedWith(kotlin.Comparator { lhs, rhs ->
                val distance1 = distanceCalculator.getDistance(
                        LatLng(lhs.lat, lhs.lng),
                        location)
                val distance2 = distanceCalculator.getDistance(
                        LatLng(rhs.lat, rhs.lng),
                        location)
                java.lang.Float.valueOf(distance1)!!.compareTo(distance2)
            })
        } catch (e: IllegalArgumentException) {
            Timber.e(e)
        }

        return null
    }
}