package ikakus.com.tbilisinav.modules.nearbystops.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class NearbyStopsAction : MviAction {
    data class GetNearbyBusStopsAction(val location: LatLng): NearbyStopsAction()
}