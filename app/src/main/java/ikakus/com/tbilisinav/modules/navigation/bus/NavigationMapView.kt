package ikakus.com.tbilisinav.modules.navigation.bus

import android.annotation.SuppressLint
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
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import ikakus.com.tbilisinav.utils.GMapHelper
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.map_view_layout.view.*


/**
 * Created by ikakus on 1/17/18.
 */
class NavigationMapView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener {

    private var mMap: GoogleMap? = null
    private var mapReady: Boolean = false
    private val mapHelper = GMapHelper()
    private val legs = ArrayList<Leg>()

    val TBILISI = LatLng(41.7151, 44.8271)

    val mapReadyPublisher = PublishSubject.create<Boolean>()!!

    init {
        LayoutInflater.from(context).inflate(R.layout.map_view_layout, this, true)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mapReady = true
        mMap?.uiSettings?.isZoomGesturesEnabled = true
        mMap?.uiSettings?.isScrollGesturesEnabled = true
        mMap?.uiSettings?.isZoomControlsEnabled = false

        val cu = CameraUpdateFactory.newLatLngZoom(TBILISI, 14f)
        mMap?.moveCamera(cu)
        mapReadyPublisher.onNext(true)

        mMap?.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        context, R.raw.map_style))

    }

    fun addLatLngPoints(array: ArrayList<LatLng>,
                        setCamera: Boolean = false,
                        color: Int) {
        addToMap(array, color, setCamera)
    }

    private fun addToMap(array: ArrayList<LatLng>,
                         color: Int,
                         setCamera: Boolean) {
        val lineOptions = PolylineOptions()
        lineOptions.addAll(array)
        lineOptions.width(15F)
        lineOptions.color(color)
        mMap?.addPolyline(lineOptions)

        if (mapReady && array.isNotEmpty()) {

            val builder = LatLngBounds.Builder()
            array.map { builder.include(it) }
            val bounds = builder.build()

            val padding = 80 // offset from edges of the map in pixels
            val cu = CameraUpdateFactory.newLatLngBounds(bounds, padding)

            if (setCamera) {
                map_container.post {
                    mMap?.moveCamera(cu)
                }
            }
        }
    }

    fun clear(){
        legs.clear()
        mMap?.clear()
    }

    fun moveTo(point: LatLng) {
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f))
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

    fun addLeg(leg: Leg, color: Int) {
        legs.add(leg)
        val route = mapHelper.decodePoly(leg.legGeometry.points)
        addToMap(ArrayList(route), color, false)
    }

    private var markerA: Marker? = null
    private var markerB: Marker? = null

    fun show(leg: Leg) {
        val array = ArrayList<LatLng>(mapHelper.decodePoly(leg.legGeometry.points))

        val start = MarkerOptions()
                .position(array.first())
                .icon(mapHelper.getMarkerIcon(context,
                        "A",
                        resources.getColor(R.color.route_start_point)))
                .anchor(0.5f, 0.5f)

        val end = MarkerOptions()
                .position(array.last())
                .icon(mapHelper.getMarkerIcon(context,
                        "B",
                        resources.getColor(R.color.route_end_point)))
                .anchor(0.5f, 0.5f)
        markerA?.remove()
        markerB?.remove()

        markerA = mMap?.addMarker(end)
        markerB = mMap?.addMarker(start)

        show(array, true)
    }

    fun show(latLng: LatLng) {
        show(latLng, true)
    }

    private fun show(latLng: LatLng, animate: Boolean) {
        val desiredPointZoom = 18f
        val zoom = if (mMap?.cameraPosition?.zoom!! > 18) {
            mMap?.cameraPosition?.zoom!!
        } else {
            desiredPointZoom
        }
        val cu = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        if (animate) {
            mMap?.animateCamera(cu)
        } else {
            mMap?.moveCamera(cu)
        }
    }

    private fun show(array: ArrayList<LatLng>, animate: Boolean) {
        val builder = LatLngBounds.Builder()
        array.map { builder.include(it) }
        val bounds = builder.build()
        val padding = 40 // offset from edges of the map in pixels
        mMap?.setPadding(padding, padding + 160, padding, padding)
        val cu = CameraUpdateFactory.newLatLngBounds(bounds, 0)
        if (animate) {
            mMap?.animateCamera(cu)
        } else {
            mMap?.moveCamera(cu)
        }
    }

    fun showFullRoute() {
        val array = ArrayList<LatLng>()
        legs.forEach {
            val arr = ArrayList<LatLng>(mapHelper.decodePoly(it.legGeometry.points))
            array.addAll(arr)
        }
        show(array, false)
    }

    @SuppressLint("MissingPermission")
    fun setLocationEnabled() {
        mMap?.isMyLocationEnabled = true
    }

}