package ikakus.com.tbilisinav.data.source

import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import io.reactivex.Single

interface RouteDataSource {
    fun getRouteDetails(busId: Int): Single<RouteModel>
    fun getRouteList(): Single<List<Int>>
}