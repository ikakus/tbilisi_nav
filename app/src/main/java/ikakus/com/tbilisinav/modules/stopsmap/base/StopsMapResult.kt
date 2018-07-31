package ikakus.com.tbilisinav.modules.stopsmap.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.data.database.models.MapBusStop

sealed class StopsMapResult : MviResult {
    sealed class GetBusStopsInBoundResult : StopsMapResult() {
        data class Success(val stops: List<MapBusStop>) : GetBusStopsInBoundResult()
        data class Failure(val error: Throwable) : GetBusStopsInBoundResult()
        object InFlight : GetBusStopsInBoundResult()
    }
}