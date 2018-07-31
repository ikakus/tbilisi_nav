package ikakus.com.tbilisinav.modules.stopinfo.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel


data class StopInfoViewState(
        val isLoading: Boolean,
        val info: BusStopInfoModel?,
        val error: Throwable?
) : MviViewState {
    companion object {
        fun idle(): StopInfoViewState {
            return StopInfoViewState(
                    isLoading = false,
                    info = null,
                    error = null
            )
        }
    }
}