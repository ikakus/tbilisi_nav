package ikakus.com.tbilisinav.data.source.navigation

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import io.reactivex.Single


class NavigationRepository(
        private val remoteDataSource: NavigationDataSource,
        private val localDataSource: NavigationDataSource
) : NavigationDataSource {

    override fun navigate(from: LatLng, to: LatLng): Single<BusNavigationResponseModel> {
        return remoteDataSource.navigate(from, to)
//        return localDataSource.navigate(from, to)
    }

}