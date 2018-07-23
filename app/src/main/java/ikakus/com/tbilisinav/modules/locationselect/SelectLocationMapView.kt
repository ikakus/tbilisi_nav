package ikakus.com.tbilisinav.modules.locationselect

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import ikakus.com.tbilisinav.R
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.map_view_layout.view.*


/**
 * Created by ikakus on 1/17/18.
 */
class SelectLocationMapView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs),
        OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    private var mapReady: Boolean = false

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
        mMap?.uiSettings?.isZoomControlsEnabled = true
        val cu = CameraUpdateFactory.newLatLngZoom(TBILISI, 14f)
        mMap?.moveCamera(cu)
        mMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style))
        mapReadyPublisher.onNext(true)

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

    fun getCenter(): LatLng? {
       return mMap?.cameraPosition?.target
    }

    @SuppressLint("MissingPermission")
    fun setLocationEnabled() {
        mMap?.isMyLocationEnabled = true
    }

}