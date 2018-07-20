package ikakus.com.tbilisinav.modules.navigation.bus.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviAction
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

sealed class NavigationAction : MviAction {
    data class BusNavigateAction(val from: LatLng, val to : LatLng): NavigationAction()
    data class SelectLegAction(val leg: Leg): NavigationAction()
    data class SelectRouteAction(val itinerary: Itinerary): NavigationAction()
}