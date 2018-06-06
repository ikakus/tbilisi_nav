package ikakus.com.tbilisinav.data.source.route.remote

import ikakus.com.tbilisinav.data.source.route.RouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class RemoteRouteDataSource(retrofit: Retrofit) : RouteDataSource {
    private var api = retrofit.create(RetrofitApi::class.java)

    override fun getRouteDetails(busId: Int): Single<RouteModel> {
        return api.getRouteInfo(busId, "bus")
    }

    override fun getRouteList(): Single<List<Int>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    interface RetrofitApi {
        @GET("routeInfo")
        fun getRouteInfo(@Query("routeNumber") busId: Int,
                         @Query("type") type: String): Single<RouteModel>

    }
}