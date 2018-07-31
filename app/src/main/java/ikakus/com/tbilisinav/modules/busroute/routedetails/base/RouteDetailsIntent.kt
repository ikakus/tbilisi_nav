package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviIntent
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop

sealed class RouteDetailsIntent : MviIntent {
    data class InitialIntent(val routeId : Int) : RouteDetailsIntent()
    data class BusStopInfoIntent(val routeStop: RouteStop) : RouteDetailsIntent()
}