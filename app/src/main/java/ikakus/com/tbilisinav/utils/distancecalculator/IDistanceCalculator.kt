package nl.tastyclub.android.tastyclub.utils.distancecalculator

import com.google.android.gms.maps.model.LatLng

/**
 * Created by ikakus on 12/29/16.
 */

interface IDistanceCalculator {
    fun getDistance(lhs: LatLng, location: LatLng): Float
}
