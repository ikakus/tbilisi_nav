package ikakus.com.tbilisinav.modules.nearbystops

import android.os.Bundle
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import kotlinx.android.synthetic.main.activity_nearby.*

class NearbyStopsActivity :
        BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        nearbyMapView.onCreate(savedInstanceState)

        nearbyMapView.cameraMoveSubject.subscribe {
            nearbyStops.setCoordinates(it.center)
        }

        nearbyStops.selectedStopPublisher.subscribe {
            nearbyMapView.moveToMarker(it.id)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        nearbyMapView.onDestroy()
    }

    override fun onPause() {
        super.onPause()
        nearbyMapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        nearbyMapView.onResume()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        nearbyMapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        nearbyMapView.onSaveInstanceState(outState)
    }
}