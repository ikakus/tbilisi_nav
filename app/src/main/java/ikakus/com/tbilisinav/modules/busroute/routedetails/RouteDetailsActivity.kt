package ikakus.com.tbilisinav.modules.busroute.routedetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsIntent
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsViewModel
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_route.*
import org.koin.android.architecture.ext.viewModel

class RouteDetailsActivity : BaseActivity(), MviView<RouteDetailsIntent, RouteDetailsViewState> {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: RouteDetailsViewModel by viewModel()

    private var busNumber = 0
    private val mapHelper = GMapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route)
        busNumber = intent.extras?.getInt(BUS_NUMBER, -1)!!
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        routeMapView.onCreate(savedInstanceState)

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

    override fun intents(): Observable<RouteDetailsIntent> = initialIntent()

    private fun initialIntent(): Observable<RouteDetailsIntent> {
        return Observable.just(RouteDetailsIntent.InitialIntent(busNumber))
    }

    override fun render(state: RouteDetailsViewState) {
        if (state.isLoading) {
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            Log.d("render", "Loading")
        }
        if (state.error != null) {
            Toast.makeText(this, state.error.message, Toast.LENGTH_SHORT).show()
            Log.d("render", state.error.message)

        }
        if (state.error == null && !state.isLoading) {
            Log.d("render", state.route.routeStops.size.toString())
            val shape = mapHelper.decodeShape(state.route.shape)
            routeMapView.addLatLngPoints(ArrayList(shape))
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