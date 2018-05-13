package ikakus.com.tbilisinav.modules.busroute.routelist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import ikakus.com.tbilisinav.BaseActivity
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.mvibase.MviView
import ikakus.com.tbilisinav.modules.busroute.routedetails.RouteDetailsActivity
import ikakus.com.tbilisinav.modules.busroute.routelist.base.RouteListIntent
import ikakus.com.tbilisinav.modules.busroute.routelist.base.RouteListViewModel
import ikakus.com.tbilisinav.modules.busroute.routelist.base.RouteListViewState
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activty_route_list.*
import org.koin.android.architecture.ext.viewModel

class RouteListActivity : BaseActivity(), MviView<RouteListIntent, RouteListViewState> {
    private lateinit var adapter: RouteListAdapter
    private val disposables: CompositeDisposable = CompositeDisposable()
    private val vModel: RouteListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activty_route_list)
        adapter = RouteListAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
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
        adapter.taskClickObservable.subscribe { RouteDetailsActivity.start(this, it) }
    }

    override fun intents(): Observable<RouteListIntent> = initialIntent()

    private fun initialIntent(): Observable<RouteListIntent> {
        return Observable.just(RouteListIntent.InitialIntent)
    }

    override fun render(state: RouteListViewState) {
        if (state.isLoading) {
            Toast.makeText(this, "Loading", Toast.LENGTH_SHORT).show()
            Log.d("render", "Loading")
        }
        if (state.error != null) {
            Toast.makeText(this, state.error.message, Toast.LENGTH_SHORT).show()
            Log.d("render", state.error.message)

        }
        if (state.error == null && !state.isLoading) {
            adapter.setData(state.route as ArrayList<Int>)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposables.dispose()
    }
}