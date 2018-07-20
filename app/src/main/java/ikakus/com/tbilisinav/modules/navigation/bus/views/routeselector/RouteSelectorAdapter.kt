package ikakus.com.tbilisinav.modules.navigation.bus.views.routeselector

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import ikakus.com.tbilisinav.modules.navigation.bus.views.steps.MiniStepView
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.route_selection_item_view.view.*
import java.util.concurrent.TimeUnit

class RouteSelectorAdapter : RecyclerView.Adapter<RouteSelectorAdapter.ViewHolder>() {
    private var dataSet = ArrayList<Itinerary>()

    private val routeClickSubject = PublishSubject.create<Itinerary>()

    val taskClickObservable: Observable<Itinerary>
        get() = routeClickSubject

    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteSelectorAdapter.ViewHolder {
        context = parent.context
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_selection_item_view, parent, false)
        return ViewHolder(textView!!)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: RouteSelectorAdapter.ViewHolder, position: Int) {
        val route = dataSet[position]
        val sumMinutes = TimeUnit.MILLISECONDS.toMinutes(route.duration.toLong())
        holder.item.tvNum.text = sumMinutes.toString()
        holder.item.container.removeAllViews()
        route.legs.forEach {
            holder.item.container.addView(MiniStepView(context!!, it))
        }

        (holder.itemView).isSelected = position == selectedPosition
//        val waitMinutes = TimeUnit.SECONDS.toMinutes(route.waitingTime.toLong())
//        val tvWait = TextView(context!!)
//        tvWait.text = " w time $waitMinutes"
//        holder.item.container.addView(tvWait)
//
//        val transitMinutes = TimeUnit.SECONDS.toMinutes(route.transitTime.toLong())
//        val tvTransit = TextView(context!!)
//        tvTransit.text = " t time $transitMinutes"
//        holder.item.container.addView(tvTransit)
//
        holder.itemView.setOnClickListener {
            routeClickSubject.onNext(dataSet[position])
        }
    }

    fun setData(list: ArrayList<Itinerary>) {
        dataSet = list
        notifyDataSetChanged()
    }

    private var selectedPosition: Int = 0

    fun setSelection(itinerary: Itinerary) {
        selectedPosition = getPosition(dataSet, itinerary)
        notifyDataSetChanged()
    }

    fun getPosition(dataSet: ArrayList<Itinerary>, itinerary: Itinerary): Int {
        if (dataSet.isNotEmpty()) {
            for (i in 0..dataSet.size) {
                if (dataSet[i] == itinerary) return i
            }
        }
        return -1
    }

    class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

}