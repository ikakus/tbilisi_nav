package ikakus.com.tbilisinav.data.source.route

import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import io.reactivex.Single

interface RouteDataSource {
    fun getRouteDetails(busId: Int): Single<RouteModel>
    fun getRouteList(): Single<List<Int>>
}