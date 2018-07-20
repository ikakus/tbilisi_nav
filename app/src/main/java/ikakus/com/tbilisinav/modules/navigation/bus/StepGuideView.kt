package ikakus.com.tbilisinav.modules.navigation.bus

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import ikakus.com.tbilisinav.data.source.navigation.models.Leg
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.navigation_guide_view_layout.view.*

class StepGuideView(context: Context, attrs: AttributeSet) :
        FrameLayout(context, attrs) {

    private var pagerAdapter: NavigationGuidePagerAdapter
    private var pageChangeListener: PageChangeListener
    private var legs: List<Leg>? = null

    val pageChangePublisher = PublishSubject.create<Leg>()

    init {
        LayoutInflater.from(context).inflate(R.layout.navigation_guide_view_layout, this, true)
        pagerAdapter = NavigationGuidePagerAdapter(context)
        viewpager!!.adapter = pagerAdapter
        pageChangeListener = PageChangeListener()
        viewpager.addOnPageChangeListener(pageChangeListener)
    }


    fun setNavigationData(itinerary: Itinerary) {
        legs = itinerary.legs
        pagerAdapter.items = itinerary.legs
        pagerAdapter.notifyDataSetChanged()
    }

    fun setSelectedLeg(selectedLeg: Leg?) {
        if(legs != null) {
            if (selectedLeg == null) {
                pageChangePublisher.onNext(legs!![0])
            } else {
                val index = legs?.indexOf(selectedLeg)
                viewpager.setCurrentItem(index!!, false)
            }
        }
    }

    private inner class PageChangeListener : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            pageChangePublisher.onNext(legs!![position])
        }

    }
}