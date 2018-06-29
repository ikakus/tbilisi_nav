package ikakus.com.tbilisinav.data.source.navigation.local

import android.content.Context
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.NavigationDataSource
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import ikakus.com.tbilisinav.utils.FileHelper
import io.reactivex.Single

class LocalNavigationDataSource(val context: Context) : NavigationDataSource {
    private val fileHelper = FileHelper()
    private val gson = Gson()
    override fun navigate(from: LatLng, to: LatLng): Single<BusNavigationResponseModel> {
        val json = fileHelper.readRawTextFile(context, R.raw.route)
        val response = gson.fromJson(json, BusNavigationResponseModel::class.java)
        return Single.just(response)
    }
}