package ikakus.com.tbilisinav.utils

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.R

class GMapHelper() {
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

        if (encoded.isNotBlank()) {
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

    fun getMarkerIcon(context: Context, letter: String, color: Int): BitmapDescriptor {
        val circleDrawable = ContextCompat.getDrawable(context, R.drawable.map_marker)

        circleDrawable?.colorFilter = PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN)

        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(
                circleDrawable?.intrinsicWidth!!,
                circleDrawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        circleDrawable.setBounds(
                0,
                0,
                circleDrawable.intrinsicWidth,
                circleDrawable.intrinsicHeight)
        circleDrawable.draw(canvas)

        val textPaint = Paint()
        textPaint.textSize = context.resources.getDimension(R.dimen.marker_text_size)
        textPaint.color = ContextCompat.getColor(context, R.color.white)
        val bounds = Rect()
        textPaint.getTextBounds(letter, 0, letter.length, bounds)

        canvas.drawText(
                letter,
                circleDrawable.intrinsicWidth / 2F - bounds.width() / 2,
                circleDrawable.intrinsicHeight / 2F + bounds.height() / 2,
                textPaint)

        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }
}