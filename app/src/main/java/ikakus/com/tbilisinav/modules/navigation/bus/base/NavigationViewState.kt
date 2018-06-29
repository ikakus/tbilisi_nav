package ikakus.com.tbilisinav.modules.navigation.bus.base

import ikakus.com.tbilisinav.core.mvibase.MviViewState
import ikakus.com.tbilisinav.data.source.navigation.models.BusNavigationResponseModel
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

data class NavigationViewState (
        val isLoading: Boolean,
        val busNavigation: BusNavigationResponseModel?,
        val selectedLeg: Leg?,
        val error: Throwable?
        ): MviViewState{
    companion object {
        fun idle(): NavigationViewState {
            return NavigationViewState(
                    isLoading = false,
                    busNavigation = null,
                    selectedLeg = null,
                    error = null
            )
        }
    }
}