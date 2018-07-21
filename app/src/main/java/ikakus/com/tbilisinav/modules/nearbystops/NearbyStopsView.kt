package ikakus.com.tbilisinav.modules.nearbystops

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsIntent
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsViewModel
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.nearby_stops_view_layout.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NearbyStopsView(context: Context, attrs: AttributeSet) :
        FrameLayout(context, attrs),
        KoinComponent,
        MviView<NearbyStopsIntent, NearbyStopsViewState> {


    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: NearbyStopsViewModel by inject()

    private val getNearbyStopsPublisher =
            PublishSubject.create<NearbyStopsIntent.GetNearbyStopsIntent>()

    val selectedStopPublisher =
            PublishSubject.create<MapBusStop>()

    var point: LatLng? = null
    var stops: List<MapBusStop>? = null

    private  var selectedStop: MapBusStop? = null

    private var pageChangeListener: PageChangeListener

    init {
        LayoutInflater.from(context).inflate(R.layout.nearby_stops_view_layout, this, true)
        button2.setOnClickListener {
            point?.let {
                getNearbyStops(it)
            }
        }
        bind()
        pageChangeListener = PageChangeListener()
        viewPager.addOnPageChangeListener(pageChangeListener)
    }

    inner class PageChangeListener : ViewPager.OnPageChangeListener{
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        }

        override fun onPageSelected(position: Int) {
            stops?.let{
                selectedStop = it[position]
                selectedStop?.let{
                    selectedStopPublisher.onNext(it)
                }

            }
        }

    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(
                vModel.states().subscribe { this.render(it) }
        )
        // Pass the UI's intents to the ViewModel
        vModel.processIntents(intents())
    }

    override fun intents(): Observable<NearbyStopsIntent> {
        return Observable.merge(initialIntent(), getNearbyStopsIntent())
    }

    private fun getNearbyStopsIntent(): Observable<NearbyStopsIntent.GetNearbyStopsIntent> {
        return getNearbyStopsPublisher
    }

    private fun getNearbyStops(location: LatLng) {
        getNearbyStopsPublisher.onNext(NearbyStopsIntent.GetNearbyStopsIntent(location))
    }

    private lateinit var pagerAdapter: NearbyStopsPagerAdapter

    override fun render(state: NearbyStopsViewState) {
        stops = state.stops

        pagerAdapter = NearbyStopsPagerAdapter(context)
        viewPager!!.adapter = pagerAdapter

        if (state.stops != null && state.stops.isNotEmpty()) {
            container.removeAllViews()
            pagerAdapter.items = state.stops
            pageChangeListener.onPageSelected(viewPager.currentItem)
        }
    }

    private fun initialIntent(): Observable<NearbyStopsIntent> {
        return Observable.just(NearbyStopsIntent.InitialIntent)
    }

    fun setCoordinates(center: LatLng?) {
        point = center
    }

}