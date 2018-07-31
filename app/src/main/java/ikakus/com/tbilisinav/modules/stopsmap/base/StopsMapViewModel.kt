package ikakus.com.tbilisinav.modules.stopsmap.base

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class StopsMapViewModel(
        private val actionProcessorHolder: StopsMapActionProcessorHolder
) : MviViewModel<StopsMapIntent, StopsMapViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<StopsMapIntent> = PublishSubject.create()
    private val statesObservable: Observable<StopsMapViewState> = compose()

    private fun compose(): Observable<StopsMapViewState> {
        return intentsSubject
                .compose<StopsMapIntent>(intentFilter)
                .map<StopsMapAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(StopsMapViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<StopsMapIntent, StopsMapIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<StopsMapIntent>(
                        shared.ofType(StopsMapIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(StopsMapIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: StopsMapIntent): StopsMapAction {
        return when (intent) {
            is StopsMapIntent.InitialIntent -> StopsMapAction.GetAllBusStopsInBoundsAction(LatLngBounds(LatLng(0.0, 0.0), LatLng(0.0, 0.0)))
            is StopsMapIntent.GetStopsInBoundsIntent -> StopsMapAction.GetAllBusStopsInBoundsAction(intent.bounds)
        }
    }

    override fun processIntents(intents: Observable<StopsMapIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<StopsMapViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: StopsMapViewState, result: StopsMapResult ->
            when (result) {
                is StopsMapResult.GetBusStopsInBoundResult -> when (result) {
                    is StopsMapResult.GetBusStopsInBoundResult.Success ->
                        previousState.copy(
                                isLoading = false,
                                stops = result.stops
                        )
                    is StopsMapResult.GetBusStopsInBoundResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is StopsMapResult.GetBusStopsInBoundResult.InFlight -> previousState.copy(isLoading = true)
                }

            }
        }
    }
}