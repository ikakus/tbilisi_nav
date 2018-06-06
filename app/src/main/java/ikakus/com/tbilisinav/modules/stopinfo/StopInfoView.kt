package ikakus.com.tbilisinav.modules.stopinfo

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoIntent
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoViewModel
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.stop_info_view_layout.view.*
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class StopInfoView(context: Context) :
        KoinComponent,
        FrameLayout(context),
        MviView<StopInfoIntent, StopInfoViewState> {

    constructor(context: Context, mapStop: MapBusStop) : this(context) {
        this.mapStop = mapStop
        getStopRoutesInfo(mapStop.id)
    }

    private lateinit var mapStop: MapBusStop
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: StopInfoViewModel by inject()
    private var adapter: StopInfoRecyclerAdapter? = null

    private val getStopInfoPublisher =
            PublishSubject.create<StopInfoIntent.GetStopInfoIntent>()

    init {
        LayoutInflater.from(context).inflate(R.layout.stop_info_view_layout, this, true)
        adapter = StopInfoRecyclerAdapter(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        bind()
    }

    private fun bind() {
        // Subscribe to the ViewModel and call render for every emitted state
        disposables.add(
                vModel.states().subscribe { this.render(it) }
        )
        // Pass the UI's intents to the ViewModel
        vModel.processIntents(intents())
    }

    override fun intents(): Observable<StopInfoIntent> {
        return Observable.merge(initialIntent(), getStopInfoIntent())
    }

    private fun getStopInfoIntent(): Observable<StopInfoIntent.GetStopInfoIntent> {
        return getStopInfoPublisher
    }

    fun getStopRoutesInfo(id: Int) {
        getStopInfoPublisher.onNext(StopInfoIntent.GetStopInfoIntent(id))
    }

    override fun render(state: StopInfoViewState) {
        if(state.isLoading){
            progress.visibility = View.VISIBLE
        }else{
            progress.visibility = View.GONE
        }

        if (state.info != null && state.info.items.isNotEmpty()) {
            adapter?.items = ArrayList(state.info.items)
        }
    }

    private fun initialIntent(): Observable<StopInfoIntent> {
        return Observable.just(StopInfoIntent.InitialIntent)
    }
}