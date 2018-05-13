package ikakus.com.tbilisinav.modules.busroute.routelist.base

sealed class RouteListResult {
    sealed class LoadRouteListResult : RouteListResult(){
        data class Success(val routeList: List<Int>) : LoadRouteListResult()
        data class Failure(val error: Throwable) : LoadRouteListResult()
        object InFlight : LoadRouteListResult()
    }
}