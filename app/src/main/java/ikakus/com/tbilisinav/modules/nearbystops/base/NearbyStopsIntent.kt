package ikakus.com.tbilisinav.modules.nearbystops.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class NearbyStopsIntent : MviIntent {
    object InitialIntent : NearbyStopsIntent()
    data class GetNearbyStopsIntent(val location: LatLng) : NearbyStopsIntent()
}