package ikakus.com.tbilisinav.modules.navigation.bus.base

import android.arch.lifecycle.ViewModel
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class NavigationViewModel(private val actionProcessorHolder: NavigationActionProcessorHolder) :
        MviViewModel<NavigationIntent, NavigationViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<NavigationIntent> = PublishSubject.create()
    private val statesObservable: Observable<NavigationViewState> = compose()

    private fun compose(): Observable<NavigationViewState> {
        return intentsSubject
                .compose<NavigationIntent>(intentFilter)
                .map<NavigationAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(NavigationViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<NavigationIntent, NavigationIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<NavigationIntent>(
                        shared.ofType(NavigationIntent.BusNavigateIntent::class.java).take(1),
                        shared.notOfType(NavigationIntent.BusNavigateIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: NavigationIntent): NavigationAction {
        return when (intent) {
            is NavigationIntent.BusNavigateIntent -> NavigationAction.BusNavigateAction(intent.from, intent.to)
            is NavigationIntent.SelectLegIntent -> NavigationAction.SelectLegAction(intent.leg)
        }
    }

    override fun processIntents(intents: Observable<NavigationIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<NavigationViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: NavigationViewState, result: NavigationResult ->
            when (result) {
                is NavigationResult.NavigateFromToResult -> when (result) {
                    is NavigationResult.NavigateFromToResult.Success ->
                        previousState.copy(
                                isLoading = false,
                                busNavigation = result.busNavigation
                        )
                    is NavigationResult.NavigateFromToResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is NavigationResult.NavigateFromToResult.InFlight -> previousState.copy(isLoading = true)
                }
                is NavigationResult.SelectLegResult.Success -> previousState.copy(selectedLeg = result.leg)
            }
        }
    }
}