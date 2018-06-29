package ikakus.com.tbilisinav.modules.locationselect

import android.os.Bundle
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.modules.navigation.bus.NavigationActivity
import kotlinx.android.synthetic.main.activty_select_location.*

class SelectLocationActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_select_location)
        selectLocationMapView.onCreate(savedInstanceState)

        button3.setOnClickListener {
            if (selectLocationMapView.from == null) {
                selectLocationMapView.from = selectLocationMapView.getCenter()
                tvFrom.text = "From: " + selectLocationMapView.from?.toString()
            } else {
                selectLocationMapView.to = selectLocationMapView.getCenter()
                tvTo.text = "To: " + selectLocationMapView.to?.toString()

                NavigationActivity.start(this,
                        selectLocationMapView.from!!,
                        selectLocationMapView.to!!)

                selectLocationMapView.from = null
                selectLocationMapView.to = null
            }
        }
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
}