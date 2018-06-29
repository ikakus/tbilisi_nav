package ikakus.com.tbilisinav.modules.navigation.bus.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class NavigationIntent : MviIntent {
    data class BusNavigateIntent(val from: LatLng, val to : LatLng) : NavigationIntent()
}