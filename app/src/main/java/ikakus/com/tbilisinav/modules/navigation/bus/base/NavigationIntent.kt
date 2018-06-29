package ikakus.com.tbilisinav.modules.navigation.bus.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviIntent
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

sealed class NavigationIntent : MviIntent {
    data class BusNavigateIntent(val from: LatLng, val to : LatLng) : NavigationIntent()
    data class SelectLegIntent(val leg: Leg) : NavigationIntent()
}