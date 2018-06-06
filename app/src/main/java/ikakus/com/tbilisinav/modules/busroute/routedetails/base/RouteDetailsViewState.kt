package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop

data class RouteDetailsViewState (
        val isLoading: Boolean,
        val route: RouteModel?,
        val routeStop: RouteStop?,
        val stopInfo: BusStopInfoModel?,
        val error: Throwable?
        ): MviViewState{
    companion object {
        fun idle(): RouteDetailsViewState {
            return RouteDetailsViewState(
                    isLoading = false,
                    route = null,
                    routeStop = null,
                    stopInfo = null,
                    error = null
            )
        }
    }
}