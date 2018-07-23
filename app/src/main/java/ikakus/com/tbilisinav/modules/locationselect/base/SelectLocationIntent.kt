package ikakus.com.tbilisinav.modules.locationselect.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class SelectLocationIntent : MviIntent {
    data class SelectStartLocationAction(val location: LatLng) : SelectLocationIntent()
    data class SelectEndLocationAction(val location: LatLng) : SelectLocationIntent()
}