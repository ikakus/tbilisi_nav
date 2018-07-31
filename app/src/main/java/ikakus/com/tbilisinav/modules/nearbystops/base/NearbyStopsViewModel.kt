package ikakus.com.tbilisinav.modules.nearbystops.base

import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class NearbyStopsViewModel(
        private val actionProcessorHolder: NearbyStopsActionProcessorHolder
) : MviViewModel<NearbyStopsIntent, NearbyStopsViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<NearbyStopsIntent> = PublishSubject.create()
    private val statesObservable: Observable<NearbyStopsViewState> = compose()

    private fun compose(): Observable<NearbyStopsViewState> {
        return intentsSubject
                .compose<NearbyStopsIntent>(intentFilter)
                .map<NearbyStopsAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(NearbyStopsViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<NearbyStopsIntent, NearbyStopsIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<NearbyStopsIntent>(
                        shared.ofType(NearbyStopsIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(NearbyStopsIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: NearbyStopsIntent): NearbyStopsAction {
        return when (intent) {
            is NearbyStopsIntent.InitialIntent -> NearbyStopsAction.GetNearbyBusStopsAction(LatLng(0.0, 0.0))
            is NearbyStopsIntent.GetNearbyStopsIntent -> NearbyStopsAction.GetNearbyBusStopsAction(intent.location)
        }
    }

    override fun processIntents(intents: Observable<NearbyStopsIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<NearbyStopsViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: NearbyStopsViewState, result: NearbyStopsResult ->
            when (result) {
                is NearbyStopsResult.GetNearbyBusStopsResult -> when (result) {
                    is NearbyStopsResult.GetNearbyBusStopsResult.Success -> previousState.copy(
                            isLoading = false,
                            stops = result.stops
                    )
                    is NearbyStopsResult.GetNearbyBusStopsResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is NearbyStopsResult.GetNearbyBusStopsResult.InFlight -> previousState.copy(isLoading = true, stops = null)
                }
            }
        }
    }
}