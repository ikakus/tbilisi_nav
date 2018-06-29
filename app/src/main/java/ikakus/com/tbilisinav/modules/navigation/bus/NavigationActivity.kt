package ikakus.com.tbilisinav.modules.navigation.bus

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
import kotlinx.android.synthetic.main.activty_navigation.*
import org.koin.android.architecture.ext.viewModel
import timber.log.Timber
import java.util.*

class NavigationActivity : BaseActivity(), MviView<NavigationIntent, NavigationViewState> {

    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: NavigationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_navigation)
        navigationMapView.onCreate(savedInstanceState)
        navigationMapView.mapReadyPublisher.subscribe {
            bind()
        }

        stepGuideView.pageChangePublisher.subscribe {
            navigationMapView.show(it)
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

    override fun intents(): Observable<NavigationIntent> = initialIntent()

    private fun initialIntent(): Observable<NavigationIntent> {

//        val from = LatLng(41.723157, 44.721624)
//        val to = LatLng(41.725975, 44.769346)
        val from = LatLng(41.725431, 44.7458504)
        val to = LatLng(41.704032, 44.789967)
        return Observable.just(NavigationIntent.BusNavigateIntent(from, to))
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
        if (state.busNavigation != null) {
            val plan = state.busNavigation.plan

            tvFrom.text = "From: " + plan.from.name
            tvTo.text = "To: " + plan.to.name

            val inter = plan.itineraries[0]
            inter.let {
                it.legs.forEach {
                    val legColor = getRandomColor()
                    navigationMapView.addLeg(it, legColor)
                }
            }
            stepGuideView.setNavigationData(inter)
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
}