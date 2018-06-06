package ikakus.com.tbilisinav.modules.busroute.routelist.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.route.RouteDataSource
import io.reactivex.Observable
import io.reactivex.ObservableTransformer


class RouteListActionProcessorHolder(
        private val routeRepository: RouteDataSource,
        private val schedulerProvider: BaseSchedulerProvider
) {
    private val getRouteProcessor =
            ObservableTransformer<RouteListAction.LoadRoutesListAction, RouteListResult.LoadRouteListResult> { actions ->
                actions.flatMap { action ->
                    routeRepository.getRouteList()
                            .toObservable()
                            .map(RouteListResult.LoadRouteListResult::Success)
                            .cast(RouteListResult.LoadRouteListResult::class.java)
                            .onErrorReturn(RouteListResult.LoadRouteListResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(RouteListResult.LoadRouteListResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<RouteListAction, RouteListResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(RouteListAction.LoadRoutesListAction::class.java).compose(getRouteProcessor)
                            .cast(RouteListResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is RouteListAction.LoadRoutesListAction }
                                            .flatMap { w ->
                                                Observable.error<RouteListResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}