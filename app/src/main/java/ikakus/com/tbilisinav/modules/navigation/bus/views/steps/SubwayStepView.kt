package ikakus.com.tbilisinav.modules.navigation.bus.views.steps

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import ikakus.com.tbilisinav.modules.navigation.bus.NavListener
import ikakus.com.tbilisinav.utils.TimeHelper
import kotlinx.android.synthetic.main.walk_step_view_layout.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SubwayStepView(context: Context) :
        FrameLayout(context),
        KoinComponent {


    private val timeHelper = TimeHelper(context)
    private val navListener: NavListener by inject()


    constructor(context: Context, leg: Leg) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.subway_step_view_layout, this, true)
        tvTime.text = timeHelper.getStringFromMillis(leg.duration.toLong())
        tvFrom.text = leg.from.name
        tvTo.text = leg.to.name

        header.setOnClickListener {
            navListener.showLeg(leg)
        }

        viewFrom.setOnClickListener {
            val latLng = LatLng(leg.from.lat, leg.from.lon)
            navListener.showPoint(latLng)
        }

        viewTo.setOnClickListener {
            val latLng = LatLng(leg.to.lat, leg.to.lon)
            navListener.showPoint(latLng)
        }
    }
}