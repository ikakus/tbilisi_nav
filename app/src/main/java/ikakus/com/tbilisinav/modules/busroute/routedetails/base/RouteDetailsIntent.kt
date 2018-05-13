package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class RouteDetailsIntent : MviIntent {
    data class InitialIntent(val routeId : Int) : RouteDetailsIntent()
}