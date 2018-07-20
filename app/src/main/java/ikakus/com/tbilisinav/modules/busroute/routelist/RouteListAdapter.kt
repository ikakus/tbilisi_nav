package ikakus.com.tbilisinav.modules.busroute.routelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import ikakus.com.tbilisinav.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class RouteListAdapter : RecyclerView.Adapter<RouteListAdapter.ViewHolder>() {
    private var dataSet = ArrayList<Int>()

    private val taskClickSubject = PublishSubject.create<Int>()

    val taskClickObservable: Observable<Int>
        get() = taskClickSubject

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteListAdapter.ViewHolder {
        val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.route_selection_item_view, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun getItemCount() = dataSet.size

    override fun onBindViewHolder(holder: RouteListAdapter.ViewHolder, position: Int) {
        holder.textView.text = dataSet[position].toString()
        holder.itemView.setOnClickListener { taskClickSubject.onNext(dataSet[position]) }
    }

    fun setData(list: ArrayList<Int>) {
        dataSet = list
        notifyDataSetChanged()
    }

    class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

}