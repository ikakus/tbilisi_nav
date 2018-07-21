package ikakus.com.tbilisinav.modules.navigation.bus.views.routeselector

import android.content.Context
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.data.source.navigation.models.Itinerary
import ikakus.com.tbilisinav.data.source.navigation.models.Plan
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.route_selector_view_layout.view.*
import java.util.concurrent.TimeUnit



class RouteSelectorView(context: Context, attrs: AttributeSet) :
        FrameLayout(context, attrs) {

    val selectedRoutePublisher = PublishSubject.create<Itinerary>()
    private var itineraries = ArrayList<Itinerary>()

    private var adapter: RouteSelectorAdapter

    init {
        LayoutInflater.from(context).inflate(R.layout.route_selector_view_layout, this, true)
        adapter = RouteSelectorAdapter()
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL))
        adapter.clickObservable.subscribe {
            setSelectedRoute(it)
            togleList()
        }
    }

    fun setPlan(plan: Plan) {
        tvFrom.text = plan.from.name
        tvTo.text = plan.to.name

        tvCounter.text = "+ ${plan.itineraries.size - 1} more"

        itineraries = ArrayList(plan.itineraries)
        val iti = itineraries.first()
        setSelectedRoute(iti)
        adapter.setData(itineraries)

        imageView2.setOnClickListener {
            togleList()
        }
        tvCounter.setOnClickListener {
            togleList()
        }
    }

    private fun togleList() {
        if (recyclerView.visibility == View.VISIBLE) {
            recyclerView.visibility = View.GONE
            tvCounter.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvCounter.visibility = View.GONE

        }
    }

    fun setSelectedRoute(iti: Itinerary) {
        selectedRoutePublisher.onNext(iti)
        adapter.setSelection(iti)
        totalTime.text = TimeUnit.MILLISECONDS.toMinutes(iti.duration.toLong()).toString()
    }
}