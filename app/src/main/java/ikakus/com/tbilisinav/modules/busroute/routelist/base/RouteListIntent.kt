package ikakus.com.tbilisinav.modules.busroute.routelist.base

import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class RouteListIntent: MviIntent {
    object InitialIntent: RouteListIntent()
}