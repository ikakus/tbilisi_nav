package ikakus.com.tbilisinav.modules.stopinfo

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.BusInfoItem
import ikakus.com.tbilisinav.utils.inflate
import kotlinx.android.synthetic.main.stop_info_item_layout.view.*
import java.util.*

class StopInfoRecyclerAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var items = ArrayList<BusInfoItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BusInfoViewHolder(parent.inflate(R.layout.stop_info_item_layout))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is BusInfoViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class BusInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BusInfoItem) = with(itemView) {
            tvName.text = item.text
            tvNumber.text = item.number.toString()
            tvTime.text = item.time.toString()
        }
    }

}