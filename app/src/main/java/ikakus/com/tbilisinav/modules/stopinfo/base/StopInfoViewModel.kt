package ikakus.com.tbilisinav.modules.stopinfo.base

import android.arch.lifecycle.ViewModel
import ikakus.com.tbilisinav.core.mvibase.MviViewModel
import ikakus.com.tbilisinav.utils.notOfType
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject

class StopInfoViewModel(
        private val actionProcessorHolder: StopInfoActionProcessorHolder
) : MviViewModel<StopInfoIntent, StopInfoViewState>, ViewModel() {

    private val intentsSubject: PublishSubject<StopInfoIntent> = PublishSubject.create()
    private val statesObservable: Observable<StopInfoViewState> = compose()

    private fun compose(): Observable<StopInfoViewState> {
        return intentsSubject
                .compose<StopInfoIntent>(intentFilter)
                .map<StopInfoAction>(this::actionFromIntent)
                .compose(actionProcessorHolder.actionProcessor)
                .scan(StopInfoViewState.idle(), reducer)
                .distinctUntilChanged()
                .replay(1)
                .autoConnect(0)
    }

    private val intentFilter: ObservableTransformer<StopInfoIntent, StopInfoIntent>
        get() = ObservableTransformer { intents ->
            intents.publish { shared ->
                Observable.merge<StopInfoIntent>(
                        shared.ofType(StopInfoIntent.InitialIntent::class.java).take(1),
                        shared.notOfType(StopInfoIntent.InitialIntent::class.java)
                )
            }
        }

    private fun actionFromIntent(intent: StopInfoIntent): StopInfoAction {
        return when (intent) {
            is StopInfoIntent.InitialIntent -> StopInfoAction.GetStopInfoAction(0)
            is StopInfoIntent.GetStopInfoIntent -> StopInfoAction.GetStopInfoAction(intent.id)
        }
    }

    override fun processIntents(intents: Observable<StopInfoIntent>) {
        intents.subscribe(intentsSubject)
    }

    override fun states(): Observable<StopInfoViewState> = statesObservable

    companion object {
        private val reducer = BiFunction { previousState: StopInfoViewState, result: StopInfoResult ->
            when (result) {
                is StopInfoResult.GetStopInfoResult -> when (result) {
                    is StopInfoResult.GetStopInfoResult.Success ->
                        previousState.copy(
                                isLoading = false,
                                info = result.info
                        )
                    is StopInfoResult.GetStopInfoResult.Failure -> previousState.copy(isLoading = false, error = result.error)
                    is StopInfoResult.GetStopInfoResult.InFlight -> previousState.copy(isLoading = true)
                }

            }
        }
    }
}