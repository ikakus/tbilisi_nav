package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.RouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsAction.GetRouteByIdAction
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsResult.GetRouteByIdResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class RouteDetailsActionProcessorHolder(
        private val routeRepository: RouteDataSource,
        private val schedulerProvider: BaseSchedulerProvider
) {
    private val getRouteProcessor =
            ObservableTransformer<GetRouteByIdAction, GetRouteByIdResult> { actions ->
                actions.flatMap { action ->
                    routeRepository.getRouteDetails(action.routeId)
                            .toObservable()
                            .map(GetRouteByIdResult::Success)
                            .cast(GetRouteByIdResult::class.java)
                            .onErrorReturn(GetRouteByIdResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(GetRouteByIdResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<RouteDetailsAction, RouteDetailsResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(GetRouteByIdAction::class.java).compose(getRouteProcessor)
                            .cast(RouteDetailsResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is GetRouteByIdAction }
                                            .flatMap { w ->
                                                Observable.error<RouteDetailsResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}