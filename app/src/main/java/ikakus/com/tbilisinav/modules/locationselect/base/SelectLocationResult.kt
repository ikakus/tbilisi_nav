package ikakus.com.tbilisinav.modules.locationselect.base

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviResult

sealed class SelectLocationResult : MviResult {
    sealed class SelectStartResult : SelectLocationResult(){
        data class Success(val location: LatLng) : SelectStartResult()
    }

    sealed class SelectEndResult : SelectLocationResult(){
        data class Success(val location: LatLng) : SelectEndResult()
    }

    sealed class ClearLocationsResult : SelectLocationResult(){
        class Success : ClearLocationsResult()
    }
}