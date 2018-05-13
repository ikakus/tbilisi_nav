package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class RouteDetailsAction : MviAction {
    data class GetRouteByIdAction(val routeId: Int): RouteDetailsAction()
}