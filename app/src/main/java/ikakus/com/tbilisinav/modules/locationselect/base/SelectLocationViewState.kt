package ikakus.com.tbilisinav.modules.locationselect.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviViewState

data class SelectLocationViewState(
        val startLocation: LatLng?,
        val endLocation: LatLng?
) : MviViewState {
    companion object {
        fun idle(): SelectLocationViewState {
            return SelectLocationViewState(
                    startLocation = null,
                    endLocation = null
            )
        }
    }
}