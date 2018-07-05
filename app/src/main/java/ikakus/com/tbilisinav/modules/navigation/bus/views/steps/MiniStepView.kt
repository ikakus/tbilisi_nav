package ikakus.com.tbilisinav.modules.navigation.bus.views.steps

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import ikakus.com.tbilisinav.data.source.navigation.models.Mode
import ikakus.com.tbilisinav.utils.DrawableHelper
import ikakus.com.tbilisinav.utils.TimeHelper
import kotlinx.android.synthetic.main.mini_step_view_layout.view.*


class MiniStepView(context: Context) :
        FrameLayout(context) {
    private val timeHelper = TimeHelper(context)

    constructor(context: Context, leg: Leg) : this(context) {
        LayoutInflater.from(context).inflate(R.layout.mini_step_view_layout, this, true)

        tvTime.text = timeHelper.getStringFromMillis(leg.duration.toLong())
        tvBusNum.visibility = View.GONE
        when (leg.mode) {
            Mode.WALK -> {
                DrawableHelper.setDrawableColor(context,miniLegLayout,R.color.walk_green)
                ivMode.setImageResource(R.drawable.ic_walk)
            }
            Mode.BUS -> {
                tvBusNum.visibility = View.VISIBLE
                tvBusNum.text = leg.route
                DrawableHelper.setDrawableColor(context,miniLegLayout,R.color.bus_yellow)
                ivMode.setImageResource(R.drawable.ic_bus)
            }
            Mode.SUBWAY -> {
//                tvBusNum.visibility = View.VISIBLE
//                tvBusNum.text = leg.route
                DrawableHelper.setDrawableColor(context,miniLegLayout,R.color.subway_blue)
                ivMode.setImageResource(R.drawable.ic_subway)
            }
        }
    }


}