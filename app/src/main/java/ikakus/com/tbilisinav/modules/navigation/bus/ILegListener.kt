package ikakus.com.tbilisinav.modules.navigation.bus

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.data.source.navigation.models.Leg

interface ILegListener {
    fun showLeg(leg: Leg)
    fun showPoint(point : LatLng)
}