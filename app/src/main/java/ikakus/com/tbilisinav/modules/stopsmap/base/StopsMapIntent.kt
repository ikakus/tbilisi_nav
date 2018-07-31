package ikakus.com.tbilisinav.modules.stopsmap.base

import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class StopsMapIntent : MviIntent {
    object InitialIntent : StopsMapIntent()
    data class GetStopsInBoundsIntent(val bounds: LatLngBounds) : StopsMapIntent()
}