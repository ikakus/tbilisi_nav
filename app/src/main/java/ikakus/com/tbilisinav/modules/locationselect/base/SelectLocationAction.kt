package ikakus.com.tbilisinav.modules.locationselect.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class SelectLocationAction : MviAction {
    data class SelectStartLocationAction(val location: LatLng) : SelectLocationAction()
    data class SelectEndLocationAction(val location: LatLng) : SelectLocationAction()
    class ClearLocationsAction : SelectLocationAction()
}
