package ikakus.com.tbilisinav.modules.nearbystops.base

import ikakus.com.tbilisinav.core.mvibase.MviResult
import ikakus.com.tbilisinav.data.database.models.MapBusStop

sealed class NearbyStopsResult : MviResult {

    sealed class GetNearbyBusStopsResult : NearbyStopsResult() {
        data class Success(val stops: List<MapBusStop>) : GetNearbyBusStopsResult()
        data class Failure(val error: Throwable) : GetNearbyBusStopsResult()
        object InFlight : GetNearbyBusStopsResult()
    }
}