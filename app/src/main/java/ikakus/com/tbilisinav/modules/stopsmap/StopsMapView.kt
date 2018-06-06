package ikakus.com.tbilisinav.modules.stopsmap

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
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapIntent
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapViewModel
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.route_map_view_layout.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import java.util.concurrent.TimeUnit


/**
 * Created by ikakus on 1/17/18.
 */
class StopsMapView(context: Context, attrs: AttributeSet) :
        KoinComponent,
        FrameLayout(context, attrs),
        MviView<StopsMapIntent, StopsMapViewState>,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnCameraMoveListener {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: StopsMapViewModel by inject()

    private var mMap: GoogleMap? = null
    private var mapReady: Boolean = false
    private var stops: HashMap<Int, MapBusStop> = HashMap()
    private var addedMarkers: HashMap<Int, Marker> = HashMap()
    private val MIN_DRAW_ZOOM = 15f
    private val MIN_ZOOM = 12F
    var routeStopClickListener: RouteStopClickListener? = null
    val cameraMoveSubject = PublishSubject.create<LatLngBounds>()
    private val getStopsInBoundsPublisher
            = PublishSubject.create<StopsMapIntent.GetStopsInBoundsIntent>()

    val TBILISI_BOUNDS = LatLngBounds(
            LatLng(41.460397169473524, 44.49836120009422),
            LatLng(42.00661755761343, 45.06872121244669))

    val TBILISI = LatLng(41.7151, 44.8271)

//    val defPosition = TBILISI
    val defPosition = LatLng(41.724971, 44.745077)


    private lateinit var defaultIcon: BitmapDescriptor
    private lateinit var selectedIcon: BitmapDescriptor

    init {
        LayoutInflater.from(context).inflate(R.layout.stops_map_view_layout, this, true)
        cameraMoveSubject
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(ImmediateSchedulerProvider().ui())
                .subscribe { getStopsInBounds(it) }
        bind()
    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(
                vModel.states().subscribe { this.render(it) }
        )
        // Pass the UI's intents to the ViewModel
        vModel.processIntents(intents())
    }

    override fun intents(): Observable<StopsMapIntent> {
        return Observable.merge(initialIntent(), getStopsInBoundsIntent())
    }

    override fun render(state: StopsMapViewState) {
        if (state.stops != null) {
            addMarkers(state.stops as ArrayList<MapBusStop>)
        }
    }

    private fun getStopsInBoundsIntent() : Observable<StopsMapIntent.GetStopsInBoundsIntent>{
        return getStopsInBoundsPublisher
    }

    private fun getStopsInBounds(bounds: LatLngBounds){
        getStopsInBoundsPublisher.onNext(StopsMapIntent.GetStopsInBoundsIntent(bounds))
    }

    private fun initialIntent(): Observable<StopsMapIntent> {
        return Observable.just(StopsMapIntent.InitialIntent)
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        mapReady = true
        mMap?.let {
            it.uiSettings?.isZoomGesturesEnabled = true
            it.uiSettings?.isScrollGesturesEnabled = true
            it.uiSettings?.isZoomControlsEnabled = true

            it.setOnCameraMoveListener(this)
            it.setOnMarkerClickListener(this)
            it.setLatLngBoundsForCameraTarget(TBILISI_BOUNDS)
            it.setMinZoomPreference(MIN_ZOOM)
            it.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    defPosition,
                    17f))
        }
        addMarkers(ArrayList(stops.values))
    }

    private fun addMarkers(points: ArrayList<MapBusStop>) {
        val markersToRemove = HashMap<Int, Marker>(addedMarkers)
        points.forEach {
            if (stops[it.id] == null) {
                stops[it.id] = it
                val colorGreen = resources.getColor(R.color.marker_green)
                val markerOptions = MarkerOptions()
                        .position(LatLng(it.lat, it.lng))
                        .icon(defaultIcon)
                        .anchor(0.5f, 1.0f)

                val marker = mMap?.addMarker(markerOptions)!!
                marker.tag = it
                addedMarkers[it.id] = marker
            }
            markersToRemove.remove(it.id)
        }
        markersToRemove.forEach {
            it.value.remove()
            stops.remove(it.key)
        }
    }

    fun moveTo(point: LatLng) {
        mMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(point, 17f))
    }

    fun moveToMarker(markerId: Int){
        moveTo(addedMarkers[markerId]?.position!!)
        selectMarker(markerId)
    }

    private var selectedMarker: Marker? = null

    fun selectMarker(markerId: Int){
        selectedMarker?.setIcon(defaultIcon)
        selectedMarker = addedMarkers[markerId]
        addedMarkers[markerId]?.setIcon(selectedIcon)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        routeStopClickListener?.selectStop(marker?.tag as RouteStop)
        return true
    }

    fun onCreate(bundle: Bundle?) {
        map_container.onCreate(bundle)
        defaultIcon = BitmapDescriptorFactory.fromResource(R.drawable.stop_icon)
        selectedIcon = BitmapDescriptorFactory.fromResource(R.drawable.stop_icon_green)
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

    override fun onCameraMove() {
        mMap?.let {
            val bounds = it.projection.visibleRegion.latLngBounds
            if (it.cameraPosition.zoom >= MIN_DRAW_ZOOM) {
                cameraMoveSubject.onNext(getExtendedBounds(bounds,0.01))
            }
        }
    }

    private fun getExtendedBounds(bounds: LatLngBounds?, exPercent: Double): LatLngBounds {
        val ne = bounds?.northeast
        val sw = bounds?.southwest

        val exNe = LatLng(addPercent(ne?.latitude, exPercent), addPercent(ne?.longitude, exPercent))
        val exSw = LatLng(substractPercent(sw?.latitude, exPercent), substractPercent(sw?.longitude, exPercent))

        return LatLngBounds(exSw, exNe)
    }

    fun addPercent(value: Double?, percent: Double): Double {
        value?.let {
            val result = (value / 100) * percent
            return value + result
        }
        return 0.0
    }

    fun substractPercent(value: Double?, percent: Double): Double {
        value?.let {
            val result = (value / 100) * percent
            return value - result
        }
        return 0.0
    }

    interface RouteStopClickListener {
        fun selectStop(stop: RouteStop)
    }

}