package ikakus.com.tbilisinav.modules.busroute.routedetails

import com.google.android.gms.maps.model.LatLng

class GMapHelper {
    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    fun decodePoly(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(lat.toDouble() / 1E5,
                    lng.toDouble() / 1E5)
            poly.add(p)
        }

        return poly
    }

    fun decodeShape(encoded: String): List<LatLng> {
        val poly = ArrayList<LatLng>()
        val innerDelimiter = ":"
        val delimiter = ","

        if(encoded.isNotBlank()) {
            val elements = encoded.split(delimiter)
            if (elements.isNotEmpty()) {
                elements.forEach {
                    val latLngStrPair = it.split(innerDelimiter)
                    val lat = latLngStrPair[1].toDouble()
                    val lng = latLngStrPair[0].toDouble()
                    poly.add(LatLng(lat, lng))
                }

            }
        }
        return poly
    }
}