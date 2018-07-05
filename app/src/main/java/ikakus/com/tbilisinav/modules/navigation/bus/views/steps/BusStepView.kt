package ikakus.com.tbilisinav.modules.navigation.bus.views.steps

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import ikakus.com.tbilisinav.utils.TimeHelper
import kotlinx.android.synthetic.main.bus_step_view_layout.view.*

class BusStepView(context: Context) :
        FrameLayout(context) {

    private val timeHelper = TimeHelper(context)

    constructor(context: Context, leg: Leg) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.bus_step_view_layout, this, true)

        tvTime.text = timeHelper.getStringFromMillis(leg.duration.toLong())
        tvBusNum.text = leg.route
        tvFrom.text = leg.from.name
        tvTo.text = leg.to.name
    }
}