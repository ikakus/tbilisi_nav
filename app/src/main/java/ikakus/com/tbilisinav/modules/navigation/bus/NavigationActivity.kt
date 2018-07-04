package ikakus.com.tbilisinav.modules.navigation.bus

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.navigation.bus.base.NavigationIntent
import ikakus.com.tbilisinav.modules.navigation.bus.base.NavigationViewModel
import ikakus.com.tbilisinav.modules.navigation.bus.base.NavigationViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activty_navigation.*
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber
import java.util.*

class NavigationActivity : BaseActivity(), MviView<NavigationIntent, NavigationViewState> {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: NavigationViewModel by viewModel()

    private val selectLegIntent = PublishSubject.create<NavigationIntent.SelectLegIntent>()
    private val selectRouteIntent = PublishSubject.create<NavigationIntent.SelectRouteIntent>()

    private var fromLatLng: LatLng? = null
    private var toLatLng: LatLng? = null

    private var isInitialized = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_navigation)
        navigationMapView.onCreate(savedInstanceState)

//        fromLatLng = intent.extras?.getParcelable(FROM_LATLNG)!!
//        toLatLng = intent.extras?.getParcelable(TO_LATLNG)!!

        navigationMapView.mapReadyPublisher.subscribe {
            bind()
        }

        stepGuideView.pageChangePublisher.subscribe {
            selectLegIntent.onNext(NavigationIntent.SelectLegIntent(it))
        }

        routeSelector.selectedRoutePublisher.subscribe {
            selectRouteIntent.onNext(NavigationIntent.SelectRouteIntent(it))
        }

    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(
                vModel.states().subscribe { this.render(it) }
        )
        // Pass the UI's intents to the ViewModel
        vModel.processIntents(intents())
    }

    override fun intents(): Observable<NavigationIntent> {
        return Observable.merge(initialIntent(), getSelectLegIntent(), getSelectRouteIntent())
    }

    private fun initialIntent(): Observable<NavigationIntent> {

//        val from = LatLng(41.723157, 44.721624)
//        val to = LatLng(41.725975, 44.769346)
        val from = LatLng(41.725431, 44.7458504)
        val to = LatLng(41.704032, 44.789967)
//        return Observable.just(NavigationIntent.BusNavigateIntent(fromLatLng!!, toLatLng!!))
        return Observable.just(NavigationIntent.BusNavigateIntent(from, to))
    }

    private fun getSelectLegIntent(): Observable<NavigationIntent.SelectLegIntent> {
        return selectLegIntent
    }

    private fun getSelectRouteIntent(): Observable<NavigationIntent.SelectRouteIntent> {
        return selectRouteIntent
    }

    override fun render(state: NavigationViewState) {
        if (state.isLoading) {
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            Timber.d("Loading")
        }

        if (state.error != null) {
            Toast.makeText(this, state.error.message, Toast.LENGTH_SHORT).show()
            Timber.d(state.error.message)
        }

        if (state.selectedLeg != null) {
            navigationMapView.show(state.selectedLeg)
        }

        if (state.busNavigation?.plan != null && !isInitialized) {
            val plan = state.busNavigation.plan
            routeSelector.setPlan(plan)
            stepGuideView.setSelectedLeg(state.selectedLeg)
            isInitialized = true
        }

        if(state.selectedRoute != null){
            navigationMapView.clear()

            state.selectedRoute.legs.forEach {
                val legColor = getRandomColor()
                navigationMapView.addLeg(it, legColor)
            }

            stepGuideView.setNavigationData(state.selectedRoute)
            selectLegIntent.onNext(NavigationIntent.SelectLegIntent(state.selectedRoute.legs.first()))
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255,
                rnd.nextInt(256),
                rnd.nextInt(256),
                rnd.nextInt(256))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        navigationMapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        navigationMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        navigationMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        navigationMapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        navigationMapView.onLowMemory()
    }

    companion object {
        private val FROM_LATLNG = "from"
        private val TO_LATLNG = "to"

        fun start(context: Context, from: LatLng, to: LatLng) {
            val intent = Intent(context, NavigationActivity::class.java)
            intent.putExtra(FROM_LATLNG, from)
            intent.putExtra(TO_LATLNG, to)
            context.startActivity(intent)
        }
    }
}