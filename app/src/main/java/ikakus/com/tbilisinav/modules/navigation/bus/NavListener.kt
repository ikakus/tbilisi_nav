package ikakus.com.tbilisinav.modules.navigation.bus

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

class NavListener : ILegListener {
    var receiver: ILegListener? = null
    override fun showLeg(leg: Leg) {
        receiver?.showLeg(leg)
    }

    override fun showPoint(point: LatLng) {
        receiver?.showPoint(point)
    }
}