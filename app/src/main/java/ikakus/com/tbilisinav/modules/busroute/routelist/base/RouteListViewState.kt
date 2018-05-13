package ikakus.com.tbilisinav.modules.busroute.routelist.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState

data class RouteListViewState (
        val isLoading: Boolean,
        val route: List<Int>,
        val error: Throwable?
): MviViewState {
    companion object {
        fun idle(): RouteListViewState {
            return RouteListViewState(
                    isLoading = false,
                    route = ArrayList(),
                    error = null
            )
        }
    }
}