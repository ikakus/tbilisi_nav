package ikakus.com.tbilisinav.modules.busroute.routedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsIntent
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsViewModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsViewState
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteStop
import ikakus.com.tbilisinav.utils.GMapHelper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_route.*
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber

class RouteDetailsActivity : BaseActivity(), MviView<RouteDetailsIntent, RouteDetailsViewState>, RouteMapView.RouteStopClickListener {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: RouteDetailsViewModel by viewModel()

    private val getStopInfoIntentPublisher = PublishSubject.create<RouteDetailsIntent.BusStopInfoIntent>()

    private var busNumber = -1
    private val mapHelper = GMapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        busNumber = intent.extras?.getInt(BUS_NUMBER, -1)!!
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        routeMapView.onCreate(savedInstanceState)
        routeMapView.routeStopClickListener = this
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

    override fun intents(): Observable<RouteDetailsIntent> {
        return Observable.merge(initialIntent(), getStopInfoIntent())
    }

    private fun initialIntent(): Observable<RouteDetailsIntent> {
        return Observable.just(RouteDetailsIntent.InitialIntent(busNumber))
    }

    private fun getStopInfoIntent(): Observable<RouteDetailsIntent.BusStopInfoIntent> {
        return getStopInfoIntentPublisher
    }

    override fun selectStop(stop: RouteStop) {
        getStopInfoIntentPublisher.onNext(RouteDetailsIntent.BusStopInfoIntent(stop))
    }

    override fun render(state: RouteDetailsViewState) {
        if (state.isLoading) {
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            Timber.d("Loading")
        }
        if (state.error != null) {
            Toast.makeText(this, state.error.message, Toast.LENGTH_SHORT).show()
            Timber.d( state.error.message)
        }
        if (state.error == null && !state.isLoading) {

            if (state.route != null) {
                val shape = mapHelper.decodeShape(state.route.shape)
                val setCamera: Boolean = state.routeStop == null
                routeMapView.addLatLngPoints(ArrayList(shape), setCamera)
                routeMapView.addMarkers(ArrayList(state.route.routeStops))
            }

            if (state.stopInfo != null) {
                stopInfoView.visibility = View.VISIBLE

                if (state.stopInfo.items.isNotEmpty()) {
                    stopInfoView.removeAllViews()
                    state.stopInfo.items.forEach {
                        val tv = TextView(this)
                        tv.text = it.number.toString() + " " + it.text + it.time.toString()
                        stopInfoView.addView(tv)
                    }
                }
            } else {
                stopInfoView.visibility = View.GONE
            }

            if(state.routeStop != null){
                val point = LatLng(
                        state.routeStop.lat,
                        state.routeStop.lng)
                routeMapView.moveTo(point)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        routeMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        routeMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        routeMapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        routeMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        routeMapView.onSaveInstanceState(outState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val BUS_NUMBER = "bus_number"
        fun start(context: Context, busNumber: Int) {
            val intent = Intent(context, RouteDetailsActivity::class.java)
            intent.putExtra(BUS_NUMBER, busNumber)
            context.startActivity(intent)
        }
    }
}