package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel

sealed class RouteDetailsResult : MviResult {
    sealed class GetRouteByIdResult : RouteDetailsResult(){
        data class Success(val route: RouteModel) : GetRouteByIdResult()
        data class Failure(val error: Throwable) : GetRouteByIdResult()
        object InFlight : GetRouteByIdResult()
    }
}