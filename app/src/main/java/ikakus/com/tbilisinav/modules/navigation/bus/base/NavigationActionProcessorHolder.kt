package ikakus.com.tbilisinav.modules.navigation.bus.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.navigation.NavigationRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class NavigationActionProcessorHolder(
        private val repository: NavigationRepository,
        private val schedulerProvider: BaseSchedulerProvider
) {
    private val getRouteProcessor =
            ObservableTransformer<NavigationAction.BusNavigateAction, NavigationResult.NavigateFromToResult> { actions ->
                actions.flatMap { action ->
                    repository.navigate(action.from, action.to)
                            .toObservable()
                            .map(NavigationResult.NavigateFromToResult::Success)
                            .cast(NavigationResult.NavigateFromToResult::class.java)
                            .onErrorReturn(NavigationResult.NavigateFromToResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(NavigationResult.NavigateFromToResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<NavigationAction, NavigationResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(NavigationAction.BusNavigateAction::class.java).compose(getRouteProcessor)
                            .cast(NavigationResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is NavigationAction.BusNavigateAction }
                                            .flatMap { w ->
                                                Observable.error<NavigationResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}