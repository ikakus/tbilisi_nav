package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import android.arch.lifecycle.ViewModel
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class RouteDetailsViewModel(
        private val actionProcessorHolder: RouteDetailsActionProcessorHolder
) : MviViewModel<RouteDetailsIntent, RouteDetailsViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<RouteDetailsIntent> = PublishSubject.create()
    private val statesObservable: Observable<RouteDetailsViewState> = compose()

    private fun compose(): Observable<RouteDetailsViewState> {
        return intentsSubject
                .compose<RouteDetailsIntent>(intentFilter)
                .map<RouteDetailsAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(RouteDetailsViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<RouteDetailsIntent, RouteDetailsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<RouteDetailsIntent>(
                        shared.ofType(RouteDetailsIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(RouteDetailsIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: RouteDetailsIntent): RouteDetailsAction {
        return when (intent) {
            is RouteDetailsIntent.InitialIntent -> RouteDetailsAction.GetRouteByIdAction(intent.routeId)
            is RouteDetailsIntent.BusStopInfoIntent -> RouteDetailsAction.GetBusStopInfoAction(intent.routeStop)
        }
    }

    override fun processIntents(intents: Observable<RouteDetailsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<RouteDetailsViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: RouteDetailsViewState, result: RouteDetailsResult ->
            when (result) {
                is RouteDetailsResult.GetRouteByIdResult -> when (result) {
                    is RouteDetailsResult.GetRouteByIdResult.Success ->
                        previousState.copy(
                                isLoading = false,
                                route = result.route
                        )
                    is RouteDetailsResult.GetRouteByIdResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is RouteDetailsResult.GetRouteByIdResult.InFlight -> previousState.copy(isLoading = true)
                }
                is RouteDetailsResult.GetBusStopInfoByIdResult.Success -> previousState.copy(
                        isLoading = false,
                        stopInfo = result.stopInfo
                )
                is RouteDetailsResult.GetBusStopInfoByIdResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                RouteDetailsResult.GetBusStopInfoByIdResult.InFlight -> previousState.copy(isLoading = true)
            }
        }
    }
}