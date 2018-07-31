package ikakus.com.tbilisinav.modules.stopinfo.base

import ikakus.com.tbilisinav.core.mvibase.MviAction

sealed class StopInfoAction : MviAction {
    data class GetStopInfoAction(val id: Int): StopInfoAction()
}