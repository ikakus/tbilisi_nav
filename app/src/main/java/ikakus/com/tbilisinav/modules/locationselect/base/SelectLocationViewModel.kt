package ikakus.com.tbilisinav.modules.locationselect.base

import android.arch.lifecycle.ViewModel
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class SelectLocationViewModel(private val actionProcessorHolder: SelectLocationActionProcessorHolder) :
        MviViewModel<SelectLocationIntent, SelectLocationViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<SelectLocationIntent> = PublishSubject.create()
    private val statesObservable: Observable<SelectLocationViewState> = compose()

    private fun compose(): Observable<SelectLocationViewState> {
        return intentsSubject
                .compose<SelectLocationIntent>(intentFilter)
                .map<SelectLocationAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(SelectLocationViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<SelectLocationIntent, SelectLocationIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<SelectLocationIntent>(
                        shared.ofType(SelectLocationIntent.SelectEndLocationAction::class.java).take(1),
                        shared.notOfType(SelectLocationIntent.SelectStartLocationAction::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: SelectLocationIntent): SelectLocationAction {
        return when (intent) {
            is SelectLocationIntent.SelectStartLocationAction -> SelectLocationAction.SelectStartLocationAction(intent.location)
            is SelectLocationIntent.SelectEndLocationAction -> SelectLocationAction.SelectEndLocationAction(intent.location)
        }
    }

    override fun processIntents(intents: Observable<SelectLocationIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<SelectLocationViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: SelectLocationViewState, result: SelectLocationResult ->
            when (result) {
                is SelectLocationResult.SelectStartResult.Success -> previousState.copy(startLocation = result.location)
                is SelectLocationResult.SelectEndResult.Success -> previousState.copy(endLocation = result.location)
            }
        }
    }
}