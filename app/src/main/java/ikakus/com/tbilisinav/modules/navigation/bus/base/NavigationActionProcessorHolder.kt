package ikakus.com.tbilisinav.modules.navigation.bus.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.navigation.NavigationRepository
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single


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

    private val getSelectedLegProcessor =
            ObservableTransformer<NavigationAction.SelectLegAction, NavigationResult> { actions ->
                actions.flatMap { action ->
                    Single.just(action.leg)
                            .toObservable()
                            .map { NavigationResult.SelectLegResult.Success(it) }
                            .cast(NavigationResult.SelectLegResult::class.java)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val getSelectedRouteProcessor =
            ObservableTransformer<NavigationAction.SelectRouteAction, NavigationResult> { actions ->
                actions.flatMap { action ->
                    Single.just(action.itinerary)
                            .toObservable()
                            .map { NavigationResult.SelectRouteResult.Success(it) }
                            .cast(NavigationResult.SelectRouteResult::class.java)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }


    internal var actionProcessor =
            ObservableTransformer<NavigationAction, NavigationResult> { actions ->
                actions.publish { shared ->
                    Observable.merge<NavigationResult>(
                            shared.ofType(NavigationAction.BusNavigateAction::class.java).compose(getRouteProcessor),
                            shared.ofType(NavigationAction.SelectLegAction::class.java).compose(getSelectedLegProcessor),
                            shared.ofType(NavigationAction.SelectRouteAction::class.java).compose(getSelectedRouteProcessor))
                            .mergeWith(
                                    shared.filter { v ->
                                        v !is NavigationAction.BusNavigateAction &&
                                                v !is NavigationAction.SelectLegAction &&
                                                v !is NavigationAction.SelectRouteAction
                                    }
                                            .flatMap { w ->
                                                Observable.error<NavigationResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}