package ikakus.com.tbilisinav.modules.locationselect

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationIntent
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewModel
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewState
import ikakus.com.tbilisinav.modules.navigation.bus.NavigationActivity
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activty_select_location.*
import org.koin.android.architecture.ext.viewModel

class SelectLocationActivity : BaseActivity(), MviView<SelectLocationIntent, SelectLocationViewState> {
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: SelectLocationViewModel by viewModel()
    private lateinit var viewState: SelectLocationViewState

    private val selectStartLocationIntent = PublishSubject.create<SelectLocationIntent.SelectStartLocationIntent>()
    private val selectEndLocationIntent = PublishSubject.create<SelectLocationIntent.SelectEndLocationIntent>()
    private val clearLocationsIntent = PublishSubject.create<SelectLocationIntent.ClearLocationsIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_select_location)
        selectLocationMapView.onCreate(savedInstanceState)

        selectLocationMapView.mapReadyPublisher.subscribe {
            enableMyLocationIfPermitted()
        }
    }

    override fun intents(): Observable<SelectLocationIntent> {
        return Observable.merge(getSelectStartLocationIntent(), getSelectEndLocationIntent(), getClearLocationsIntent())
    }

    override fun render(state: SelectLocationViewState) {
        viewState = state
        selectLocationMapView.setStartPoint(state.startLocation)
        selectLocationMapView.setEndPoint(state.endLocation)

        if (isStart(viewState)) {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_a))
            buttonSelectLocation.text = getString(R.string.select_start)
            buttonSelectLocation.setBackgroundDrawable(resources.getDrawable(R.drawable.selector_color_start))
        } else {
            imageView.setImageDrawable(resources.getDrawable(R.drawable.ic_pin_b))
            buttonSelectLocation.text = getString(R.string.select_end)
            buttonSelectLocation.setBackgroundDrawable(resources.getDrawable(R.drawable.selector_color_end))
        }

        if (state.startLocation == null) {
            tvFrom.text = getString(R.string.not_selected)
        } else {
            tvFrom.text = state.startLocation.toString()
        }

        if (state.endLocation == null) {
            tvTo.text = getString(R.string.not_selected)
        } else {
            tvTo.text = state.endLocation.toString()
        }

        // Dunno if its Ok to handle navigation like this in render func
        // IMHO this version is better than previous one

        if (state.startLocation != null && state.endLocation != null) {

            handleNavigation()
            // finishing Activity because I don't how to reset
            // viewModel so it does not repeat previous states
            finish()
        }
    }

    private fun getSelectStartLocationIntent():
            Observable<SelectLocationIntent.SelectStartLocationIntent> {
        return selectStartLocationIntent
    }

    private fun getSelectEndLocationIntent():
            Observable<SelectLocationIntent.SelectEndLocationIntent> {
        return selectEndLocationIntent
    }

    private fun getClearLocationsIntent():
            Observable<SelectLocationIntent.ClearLocationsIntent> {
        return clearLocationsIntent
    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(
                vModel.states().subscribe { this.render(it) }
        )
        // Pass the UI's intents to the ViewModel
        vModel.processIntents(intents())

        buttonSelectLocation.setOnClickListener {
            handleLocationSelection()
        }
    }

    private fun handleNavigation() {
        if (!isStart(viewState)) {
            NavigationActivity.start(this,
                    viewState.startLocation!!,
                    viewState.endLocation!!)

        }
    }

    private fun handleLocationSelection() {
        if (isStart(viewState)) {
            selectStartLocationIntent
                    .onNext(SelectLocationIntent
                            .SelectStartLocationIntent(selectLocationMapView.getCenter()!!))
        } else {
            selectEndLocationIntent
                    .onNext(SelectLocationIntent
                            .SelectEndLocationIntent(selectLocationMapView.getCenter()!!))
        }
    }

    private fun isStart(viewState: SelectLocationViewState): Boolean {
        if (viewState.startLocation == null) return true
        return false

    }

    override fun onBackPressed() {
        if (isStart(viewState)) {
            super.onBackPressed()
        } else {
            clearLocationsIntent.onNext(SelectLocationIntent.ClearLocationsIntent())
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        selectLocationMapView.onSaveInstanceState(outState)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
        selectLocationMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        selectLocationMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        selectLocationMapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        selectLocationMapView.onLowMemory()
    }

    private val LOCATION_PERMISSION_REQUEST_CODE = 111

    private fun enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            bind()
            selectLocationMapView.setLocationEnabled()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocationIfPermitted()
                }
                return
            }
        }
    }

    companion object {
        private val FROM_LATLNG = "from"
        private val TO_LATLNG = "to"

        fun start(context: Context, from: LatLng?, to: LatLng?) {
            val intent = Intent(context, SelectLocationActivity::class.java)
            from?.let { intent.putExtra(FROM_LATLNG, it) }
            to?.let { intent.putExtra(TO_LATLNG, it) }
            context.startActivity(intent)
        }
    }
}