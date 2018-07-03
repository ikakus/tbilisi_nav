package ikakus.com.tbilisinav.modules.navigation.bus.views.routeselector

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class RouteSelectorAdapter : RecyclerView.Adapter<RouteSelectorAdapter.ViewHolder>() {
    private var dataSet = ArrayList<Itinerary>()

    private val routeClickSubject = PublishSubject.create<Itinerary>()

    val taskClickObservable: Observable<Itinerary>
        get() = routeClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteSelectorAdapter.ViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.my_text_view, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: RouteSelectorAdapter.ViewHolder, position: Int) {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(dataSet[position].duration.toLong())
        holder.textView.text = minutes.toString()
        holder.itemView.setOnClickListener { routeClickSubject.onNext(dataSet[position]) }
    }

    fun setData(list: ArrayList<Itinerary>) {
        dataSet = list
        notifyDataSetChanged()
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

}