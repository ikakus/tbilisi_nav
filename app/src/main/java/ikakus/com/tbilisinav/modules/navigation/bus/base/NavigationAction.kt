package ikakus.com.tbilisinav.modules.navigation.bus.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class NavigationAction : MviAction {
    data class BusNavigateAction(val from: LatLng, val to : LatLng): NavigationAction()
}