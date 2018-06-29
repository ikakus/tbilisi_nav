package ikakus.com.tbilisinav.data.source.navigation

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import io.reactivex.Single

interface NavigationDataSource {
    fun navigate(from: LatLng, to: LatLng) : Single<BusNavigationResponseModel>
}