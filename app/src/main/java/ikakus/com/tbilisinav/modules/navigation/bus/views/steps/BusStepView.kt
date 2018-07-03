package ikakus.com.tbilisinav.modules.navigation.bus.views.steps

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import kotlinx.android.synthetic.main.bus_step_view_layout.view.*
import java.util.concurrent.TimeUnit

class BusStepView(context: Context) :
        FrameLayout(context) {

    constructor(context: Context, leg: Leg) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.bus_step_view_layout, this, true)

        tvTime.text = TimeUnit.MILLISECONDS.toMinutes(leg.duration.toLong()).toString() + " min"
        tvBusNum.text = leg.route
        tvFrom.text = leg.from.name
        tvTo.text = leg.to.name
    }
}