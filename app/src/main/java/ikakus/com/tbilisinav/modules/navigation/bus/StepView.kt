package ikakus.com.tbilisinav.modules.navigation.bus

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import ikakus.com.tbilisinav.data.source.navigation.models.Mode
import ikakus.com.tbilisinav.modules.navigation.bus.views.BusStepView
import ikakus.com.tbilisinav.modules.navigation.bus.views.SubwayStepView
import ikakus.com.tbilisinav.modules.navigation.bus.views.WalkStepView
import kotlinx.android.synthetic.main.step_view_layout.view.*

class StepView(context: Context) :
        FrameLayout(context) {

    constructor(context: Context, leg: Leg) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.step_view_layout, this, true)

        when {
            leg.mode == Mode.BUS -> stepTypeContainer.addView(BusStepView(context, leg))
            leg.mode == Mode.WALK -> stepTypeContainer.addView(WalkStepView(context, leg))
            leg.mode == Mode.SUBWAY -> stepTypeContainer.addView(SubwayStepView(context, leg))
            else -> {
                val tv = TextView(context)
                tv.text = leg.from.name
                stepTypeContainer.addView(tv)
            }
        }
    }
}