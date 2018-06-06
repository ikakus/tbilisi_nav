package nl.tastyclub.android.tastyclub.utils.distancecalculator

import android.location.Location
import com.google.android.gms.maps.model.LatLng

/**
 * Created by ikakus on 12/29/16.
 */

class DistanceCalculatorImpl : IDistanceCalculator {
    override fun getDistance(lhs: LatLng, location: LatLng): Float {
        val distanceResult1 = FloatArray(1)
        Location.distanceBetween(
                location.latitude,
                location.longitude,
                lhs.latitude,
                lhs.longitude,
                distanceResult1)
        return distanceResult1[0]
    }
}
