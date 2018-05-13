package ikakus.com.tbilisinav.modules.busroute.routelist.base

import android.arch.lifecycle.ViewModel
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class RouteListViewModel(private val actionProcessorHolder: RouteListActionProcessorHolder) :
        MviViewModel<RouteListIntent, RouteListViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<RouteListIntent> = PublishSubject.create()
    private val statesObservable: Observable<RouteListViewState> = compose()

    private fun compose(): Observable<RouteListViewState> {
        return intentsSubject
                .compose<RouteListIntent>(intentFilter)
                .map<RouteListAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(RouteListViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<RouteListIntent, RouteListIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<RouteListIntent>(
                        shared.ofType(RouteListIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(RouteListIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: RouteListIntent): RouteListAction {
        return when (intent) {
            is RouteListIntent.InitialIntent -> RouteListAction.LoadRoutesListAction
        }
    }

    override fun processIntents(intents: Observable<RouteListIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<RouteListViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: RouteListViewState, result: RouteListResult ->
            when (result) {
                is RouteListResult.LoadRouteListResult -> when (result) {
                    is RouteListResult.LoadRouteListResult.Success ->
                        previousState.copy(
                                isLoading = false,
                                route = result.routeList
                        )
                    is RouteListResult.LoadRouteListResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is RouteListResult.LoadRouteListResult.InFlight -> previousState.copy(isLoading = true)
                }
            }
        }
    }
}