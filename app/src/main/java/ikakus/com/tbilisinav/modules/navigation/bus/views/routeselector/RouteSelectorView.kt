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
    var isListOpen = false
     private set

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
            toggleList()
        }
    }

    fun setPlan(plan: Plan) {
        tvFrom.text = plan.from.name
        tvTo.text = plan.to.name

        tvCounter.text = resources.getString(R.string.more_routes,plan.itineraries.size - 1)

        itineraries = ArrayList(plan.itineraries)
        val iti = itineraries.first()
        setSelectedRoute(iti)
        adapter.setData(itineraries)

        imageView2.setOnClickListener {
            toggleList()
        }
        counterLayout.setOnClickListener {
            toggleList()
        }
    }

    fun toggleList() {
        if (recyclerView.visibility == View.VISIBLE) {
            isListOpen = false
            recyclerView.visibility = View.GONE
            counterLayout.visibility = View.VISIBLE
        } else {
            isListOpen = true
            recyclerView.visibility = View.VISIBLE
            counterLayout.visibility = View.GONE
        }
    }

    fun setSelectedRoute(iti: Itinerary) {
        selectedRoutePublisher.onNext(iti)
        adapter.setSelection(iti)
        totalTime.text = TimeUnit.MILLISECONDS.toMinutes(iti.duration.toLong()).toString()
    }
}