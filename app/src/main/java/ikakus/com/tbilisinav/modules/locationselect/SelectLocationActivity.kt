package ikakus.com.tbilisinav.modules.locationselect

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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

    private val selectStartLocationIntent
            = PublishSubject.create<SelectLocationIntent.SelectStartLocationIntent>()
    private val selectEndLocationIntent
            = PublishSubject.create<SelectLocationIntent.SelectEndLocationIntent>()
    private val clearLocationsIntent
            = PublishSubject.create<SelectLocationIntent.ClearLocationsIntent>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_select_location)
        selectLocationMapView.onCreate(savedInstanceState)

        selectLocationMapView.mapReadyPublisher.subscribe {
            enableMyLocationIfPermitted()
        }

        buttonSelectLocation.setOnClickListener {
            if (isStart) {
                selectStartLocationIntent
                        .onNext(SelectLocationIntent.SelectStartLocationIntent(selectLocationMapView.getCenter()!!))
            } else {
                selectEndLocationIntent
                        .onNext(SelectLocationIntent.SelectEndLocationIntent(selectLocationMapView.getCenter()!!))
            }
        }
    }

    override fun intents(): Observable<SelectLocationIntent> {
        return Observable.merge(getSelectStartLocationIntent(), getSelectEndLocationIntent(), getClearLocationsIntent())
    }

    private var isStart: Boolean = true

    override fun render(state: SelectLocationViewState) {
        isStart = true
        if (state.startLocation == null) {
            tvFrom.text = getString(R.string.not_selected)
            buttonSelectLocation.text = getString(R.string.select_start)

        } else {
            tvFrom.text =  state.startLocation.toString()
            buttonSelectLocation.text = getString(R.string.select_end)
            isStart = false
        }

        if (state.endLocation == null) {
            tvTo.text = getString(R.string.not_selected)
        } else {
            tvTo.text = state.endLocation.toString()
        }

        if (state.startLocation != null && state.endLocation != null) {
            NavigationActivity.start(this,
                    state.startLocation,
                    state.endLocation)
            clearLocationsIntent.onNext(SelectLocationIntent.ClearLocationsIntent())
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
}