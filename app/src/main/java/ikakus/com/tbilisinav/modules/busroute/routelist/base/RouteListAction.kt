package ikakus.com.tbilisinav.modules.busroute.routelist.base

import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class RouteListAction :MviAction {
    object LoadRoutesListAction: RouteListAction()
}