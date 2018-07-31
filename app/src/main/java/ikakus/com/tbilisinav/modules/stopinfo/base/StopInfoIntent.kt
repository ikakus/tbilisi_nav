package ikakus.com.tbilisinav.modules.stopinfo.base

import ikakus.com.tbilisinav.core.mvibase.MviIntent

sealed class StopInfoIntent : MviIntent {
    object InitialIntent : StopInfoIntent()
    data class GetStopInfoIntent(val id: Int) : StopInfoIntent()
}