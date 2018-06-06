package ikakus.com.tbilisinav.modules.stopinfo.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusStopInfoModel

sealed class StopInfoResult : MviResult {
    sealed class GetStopInfoResult : StopInfoResult() {
        data class Success(val info: BusStopInfoModel) : GetStopInfoResult()
        data class Failure(val error: Throwable) : GetStopInfoResult()
        object InFlight : GetStopInfoResult()
    }
}