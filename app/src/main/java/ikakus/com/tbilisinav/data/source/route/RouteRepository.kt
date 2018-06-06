package ikakus.com.tbilisinav.data.source.route

import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import io.reactivex.Single

class RouteRepository(
        private val remoteDataSource: RouteDataSource,
        private val localDataSource: RouteDataSource
) : RouteDataSource {

    override fun getRouteDetails(busId: Int): Single<RouteModel> {
        return remoteDataSource.getRouteDetails(busId)
    }

    override fun getRouteList(): Single<List<Int>> {
        //Using local data is we don't have remote method for this
        return localDataSource.getRouteList()
    }

}