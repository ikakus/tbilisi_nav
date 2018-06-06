package ikakus.com.tbilisinav.modules.nearbystops.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.data.database.models.MapBusStop


data class NearbyStopsViewState(
        val isLoading: Boolean,
        val stops: List<MapBusStop>?,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): NearbyStopsViewState {
            return NearbyStopsViewState(
                    isLoading = false,
                    stops = null,
                    error = null
            )
        }
    }
}