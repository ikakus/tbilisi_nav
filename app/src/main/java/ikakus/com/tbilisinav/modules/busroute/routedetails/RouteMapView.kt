package ikakus.com.tbilisinav.modules.busroute.routedetails

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import ikakus.com.tbilisinav.utils.GMapHelper
import kotlinx.android.synthetic.main.map_view_layout.view.*


/**
 * Created by ikakus on 1/17/18.
 */
class RouteMapView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener {

    private var mMap: GoogleMap? = null
    private var mLineOptions: PolylineOptions? = null
    private var mapReady: Boolean = false
    private val mapHelper = GMapHelper()
    private var stops: ArrayList<RouteStop> = ArrayList()
    var routeStopClickListener: RouteStopClickListener? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.map_view_layout, this, true)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mapReady = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isScrollGesturesEnabled = true
        mMap?.uiSettings?.isZoomControlsEnabled = true

        addMarkers(stops)
        mLineOptions?.let {
            mMap?.addPolyline(it)
        }

        mMap?.setOnMarkerClickListener(this)

    }

    private lateinit var latLngArray: ArrayList<LatLng>

    fun addMarkers(points: ArrayList<RouteStop>) {
        stops = points
        points.forEach {
            val colorGreen = resources.getColor(R.color.marker_green)
            val colorYellow = resources.getColor(R.color.marker_yellow)
            val color = if (it.forward) colorGreen else colorYellow
            val marker = MarkerOptions()
                    .position(LatLng(it.lat, it.lng))
                    .icon(mapHelper.getMarkerIcon(
                            context,
                            it.stopId.toString(),
                            color))
                    .anchor(0.5f, 0.5f)

            mMap?.addMarker(marker)?.tag = it
        }
    }

    fun addRoutePoints(points: ArrayList<RouteStop>) {
        latLngArray = ArrayList()
        points.mapTo(latLngArray) { LatLng(it.lat, it.lng) }
        addLatLngPoints(latLngArray)
    }

    fun addLatLngPoints(latLngArray: ArrayList<LatLng>, setCamera :Boolean = false) {
        mLineOptions = PolylineOptions()
        mLineOptions?.addAll(latLngArray)
        mLineOptions?.width(5F)
        mLineOptions?.color(resources.getColor(R.color.route_red))
        mMap?.addPolyline(mLineOptions)

        if (mapReady && latLngArray.isNotEmpty()) {

            val builder = LatLngBounds.Builder()
            latLngArray.map { builder.include(it) }
            val bounds = builder.build()

            val padding = 140 // offset from edges of the map in pixels
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            if(setCamera) {
                map_container.post({
                    mMap?.moveCamera(cu)
                })
            }
        }
    }

    fun moveTo(point: LatLng){
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f))
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        routeStopClickListener?.selectStop(marker?.tag as RouteStop)
        return true
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

    interface RouteStopClickListener{
        fun selectStop(stop :RouteStop)
    }

}