package ikakus.com.tbilisinav.modules.locationselect

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.widget.Toast
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationIntent
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewModel
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activty_select_location.*
import org.koin.android.architecture.ext.viewModel

class SelectLocationActivity : BaseActivity(), MviView<SelectLocationIntent, SelectLocationViewState> {
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: SelectLocationViewModel by viewModel()

    private val selectStartLocationIntent = PublishSubject.create<SelectLocationIntent.SelectStartLocationAction>()
    private val selectEndLocationIntent = PublishSubject.create<SelectLocationIntent.SelectEndLocationAction>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_select_location)
        selectLocationMapView.onCreate(savedInstanceState)
        bind()

        button3.setOnClickListener {
            if (isStart) {
                selectStartLocationIntent.
                        onNext(SelectLocationIntent.
                                SelectStartLocationAction(selectLocationMapView.getCenter()!!))
            } else {
                selectEndLocationIntent.
                        onNext(SelectLocationIntent.
                                SelectEndLocationAction(selectLocationMapView.getCenter()!!))
            }
            //            if (selectLocationMapView.from == null) {
//                selectLocationMapView.from = selectLocationMapView.getCenter()
//                tvFrom.text = "From: " + selectLocationMapView.from?.toString()
//            } else {
//                selectLocationMapView.to = selectLocationMapView.getCenter()
//                tvTo.text = "To: " + selectLocationMapView.to?.toString()
//
//                NavigationActivity.start(this,
//                        selectLocationMapView.from!!,
//                        selectLocationMapView.to!!)
//
//                selectLocationMapView.from = null
//                selectLocationMapView.to = null
//            }
        }
    }

    override fun intents(): Observable<SelectLocationIntent> {
        return Observable.merge(getSelectStartLocationIntent(), getSelectEndLocationIntent())
    }

    private var isStart: Boolean = true

    override fun render(state: SelectLocationViewState) {
        if (state.startLocation == null) {
            Toast.makeText(this, "First ", Toast.LENGTH_SHORT).show()
        } else {
            tvFrom.text = "From: " + state.startLocation.toString()
            isStart = false
        }

        if (state.endLocation == null) {
            Toast.makeText(this, "Second ", Toast.LENGTH_SHORT).show()
        } else {
            tvTo.text = "To: " + state.endLocation.toString()
        }
    }

    private fun getSelectStartLocationIntent():
            Observable<SelectLocationIntent.SelectStartLocationAction> {
        return selectStartLocationIntent
    }

    private fun getSelectEndLocationIntent():
            Observable<SelectLocationIntent.SelectEndLocationAction> {
        return selectEndLocationIntent
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
//            selectLocationMapView.setLocationEnabled()
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