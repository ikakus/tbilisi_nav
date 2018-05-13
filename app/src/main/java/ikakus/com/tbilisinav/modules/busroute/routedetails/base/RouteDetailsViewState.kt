package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel

data class RouteDetailsViewState (
        val isLoading: Boolean,
        val route: RouteModel,
        val error: Throwable?
        ): MviViewState{
    companion object {
        fun idle(): RouteDetailsViewState {
            return RouteDetailsViewState(
                    isLoading = false,
                    route = RouteModel(),
                    error = null
            )
        }
    }
}