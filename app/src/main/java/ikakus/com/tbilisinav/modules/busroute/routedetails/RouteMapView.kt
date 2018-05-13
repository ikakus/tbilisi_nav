package ikakus.com.tbilisinav.modules.busroute.routedetails

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import kotlinx.android.synthetic.main.route_map_view_layout.view.*


/**
 * Created by ikakus on 1/17/18.
 */
class RouteMapView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mLineOptions: PolylineOptions
    private var mapReady: Boolean = false

    init {
        LayoutInflater.from(context).inflate(R.layout.route_map_view_layout, this, true)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mapReady = true
        mMap.uiSettings.isZoomGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true

        if (this::mLineOptions.isInitialized) {
            mMap.addPolyline(mLineOptions)
        }
    }

    private lateinit var latLngArray: ArrayList<LatLng>

    fun addRoutePoints(points: ArrayList<RouteStop>) {
        latLngArray = ArrayList()
        points.mapTo(latLngArray) { LatLng(it.lat, it.lng) }
        addLatLngPoints(latLngArray)
    }

    fun addLatLngPoints(latLngArray: ArrayList<LatLng>) {
        mLineOptions = PolylineOptions()
        mLineOptions.addAll(latLngArray)
        mLineOptions.width(5F)
        mLineOptions.color(resources.getColor(R.color.route_red))
        if (this::mMap.isInitialized) {
            mMap.addPolyline(mLineOptions)
        }

        if (mapReady) {

            val builder = LatLngBounds.Builder()
            latLngArray.map { builder.include(it) }
            val bounds = builder.build()

            val padding = 140 // offset from edges of the map in pixels
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            map_container.post({
                mMap.moveCamera(cu)
            })
        }
    }

    fun onCreate(bundle: Bundle?) {
        map_container.onCreate(bundle)
    }

    fun onResume() {
        map_container.onResume()
        map_container.getMapAsync(this)
    }

    fun onPause() {
        map_container.onPause()
    }

    fun onDestroy() {
        map_container.onDestroy()
    }

    fun onSaveInstanceState(bundle: Bundle?) {
        map_container.onSaveInstanceState(bundle)
    }

    fun onLowMemory() {
        map_container.onLowMemory()
    }

    override fun onMapClick(p0: LatLng?) {

    }

}