package ikakus.com.tbilisinav.modules.stopsmap.base

import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class StopsMapAction : MviAction {
    data class GetAllBusStopsInBoundsAction(val bounds: LatLngBounds): StopsMapAction()
}