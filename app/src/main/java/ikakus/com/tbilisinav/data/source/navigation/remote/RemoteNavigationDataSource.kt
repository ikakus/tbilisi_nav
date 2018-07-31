package ikakus.com.tbilisinav.data.source.navigation.remote

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.data.source.navigation.NavigationDataSource
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

class RemoteNavigationDataSource(retrofit: Retrofit) : NavigationDataSource {
    private var api = retrofit.create(RetrofitApi::class.java)

    override fun navigate(from: LatLng, to: LatLng): Single<BusNavigationResponseModel> {
        val fromStr = from.latitude.toString() + "," + from.longitude.toString()
        val toStr = to.latitude.toString() + "," + to.longitude.toString()
        return api.navigate(fromStr, toStr)
    }

    interface RetrofitApi {
        // FULL

        //http://transit.ttc.com.ge/opentripplanner-api-webapp/ws/
        // plan?_dc=1528354642800&
        // from.form.id=41.723157,44.721624&
        // to.form.id=41.725975,44.769346&
        // arriveBy=false&
        // time=10:57&
        // ui_date=6/7/2018&
        // mode=TRANSIT,WALK&
        // optimize=QUICK&
        // maxWalkDistance=500&
        // date=2018-06-07&
        // routerId=&
        // toPlace=41.725975,44.769346&
        // fromPlace=41.723157,44.721624


        // SHORT

        //http://transit.ttc.com.ge/opentripplanner-api-webapp/ws/
        // plan?
        // toPlace=41.725975,44.769346&
        // fromPlace=41.723157,44.721624


        @GET("http://transiten.ttc.com.ge/opentripplanner-api-webapp/ws/plan?")
        fun navigate(@Query("fromPlace") from: String,
                     @Query("toPlace") to: String): Single<BusNavigationResponseModel>

    }
}