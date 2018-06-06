package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop

sealed class RouteDetailsResult : MviResult {
    sealed class GetRouteByIdResult : RouteDetailsResult(){
        data class Success(val route: RouteModel) : GetRouteByIdResult()
        data class Failure(val error: Throwable) : GetRouteByIdResult()
        object InFlight : GetRouteByIdResult()
    }

    sealed class GetBusStopInfoByIdResult : RouteDetailsResult(){
        class Success(val stopInfo: BusStopInfoModel, routeStop: RouteStop) : GetBusStopInfoByIdResult()
        data class Failure(val error: Throwable) : GetBusStopInfoByIdResult()
        object InFlight : GetBusStopInfoByIdResult()
    }
}