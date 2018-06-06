package ikakus.com.tbilisinav.data.source.busstops.remote

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.data.source.busstops.BusStopDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusInfoParser
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

class RemoteBusStopDataSource(retrofit: Retrofit) : BusStopDataSource {
    private var api = retrofit.
            create(RetrofitApi::class.java)
    private val parser = BusInfoParser()

    override fun getAllStopsInBounds(bounds: LatLngBounds): Single<List<MapBusStop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNearby(location: LatLng): Single<MutableList<MapBusStop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllStops(): Single<List<MapBusStop>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getBusStopInfo(id: Int): Single<BusStopInfoModel> {
        return api.getBusStopInfo(id).map {
            val str = it.string()
            Timber.d("getBusStopInfo %s", str)
            parser.parseBusStopInfo(str)
            BusStopInfoModel(parser.parseBusStopInfo(str))
        }
    }

    interface RetrofitApi {
        @GET("servlet/stopArrivalTimesServlet")
        fun getBusStopInfo(@Query("stopId") busId: Int): Single<ResponseBody>

    }
}