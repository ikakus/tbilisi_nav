package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviAction
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop

sealed class RouteDetailsAction : MviAction {
    data class GetRouteByIdAction(val routeId: Int): RouteDetailsAction()
    data class GetBusStopInfoAction(val routeStop: RouteStop): RouteDetailsAction()
}