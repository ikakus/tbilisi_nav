package ikakus.com.tbilisinav.modules.navigation.bus.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel

sealed class NavigationResult : MviResult {
    sealed class NavigateFromToResult : NavigationResult(){
        data class Success(val busNavigation: BusNavigationResponseModel) : NavigateFromToResult()
        data class Failure(val error: Throwable) : NavigateFromToResult()
        object InFlight : NavigateFromToResult()
    }

}