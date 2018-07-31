package ikakus.com.tbilisinav.modules.navigation.bus.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

sealed class NavigationResult : MviResult {
    sealed class NavigateFromToResult : NavigationResult(){
        data class Success(val busNavigation: BusNavigationResponseModel) : NavigateFromToResult()
        data class Failure(val error: Throwable) : NavigateFromToResult()
        object InFlight : NavigateFromToResult()
    }

    sealed class SelectLegResult : NavigationResult(){
        data class Success(val leg: Leg) : SelectLegResult()
    }

    sealed class SelectRouteResult : NavigationResult(){
        data class Success(val itinerary: Itinerary) : SelectRouteResult()
    }
}