package ikakus.com.tbilisinav.modules.stopsmap.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.data.database.models.MapBusStop


data class StopsMapViewState(
        val isLoading: Boolean,
        val stops: List<MapBusStop>?,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): StopsMapViewState {
            return StopsMapViewState(
                    isLoading = false,
                    stops = null,
                    error = null
            )
        }
    }
}