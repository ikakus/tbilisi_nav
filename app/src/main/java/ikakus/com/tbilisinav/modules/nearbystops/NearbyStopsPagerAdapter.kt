package ikakus.com.tbilisinav.modules.nearbystops

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.stopinfo.StopInfoView

/**
 * Created by ikakus on 1/30/17.
 */

class NearbyStopsPagerAdapter(private val context: Context) : PagerAdapter() {

    var items: List<MapBusStop> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val stopInfoView = StopInfoView(context, items[position])
        collection.addView(stopInfoView)
        return stopInfoView
    }

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return ""
    }

}
