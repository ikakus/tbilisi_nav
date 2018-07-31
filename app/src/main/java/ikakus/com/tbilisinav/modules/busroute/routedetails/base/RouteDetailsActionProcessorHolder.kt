package ikakus.com.tbilisinav.modules.busroute.routedetails.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopRepository
import ikakus.com.tbilisinav.data.source.route.RouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsAction.GetBusStopInfoAction
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsAction.GetRouteByIdAction
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsResult.GetBusStopInfoByIdResult
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsResult.GetRouteByIdResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class RouteDetailsActionProcessorHolder(
        private val routeRepository: RouteDataSource,
        private val busStopRepository: BusStopRepository,
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

    private val getStopInfoProcessor =
            ObservableTransformer<GetBusStopInfoAction, GetBusStopInfoByIdResult> { actions ->
                actions.flatMap { action ->
                    busStopRepository.getBusStopInfo(action.routeStop.stopId)
                            .toObservable()
                            .map { GetBusStopInfoByIdResult.Success(it, action.routeStop) }
                            .cast(GetBusStopInfoByIdResult::class.java)
                            .onErrorReturn(GetBusStopInfoByIdResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(GetBusStopInfoByIdResult.InFlight)
                }
            }


    internal var actionProcessor =
            ObservableTransformer<RouteDetailsAction, RouteDetailsResult> { actions ->
                actions.publish { shared ->
                    Observable.merge<RouteDetailsResult>(
                            shared.ofType(GetRouteByIdAction::class.java).compose(getRouteProcessor),
                            shared.ofType(GetBusStopInfoAction::class.java).compose(getStopInfoProcessor))
                            .mergeWith(
                                    // Error for not implemented actions
                                    shared.filter { v ->
                                        (v !is GetRouteByIdAction
                                                && v !is GetBusStopInfoAction
                                                )
                                    }
                                            .flatMap { w ->
                                                Observable.error<RouteDetailsResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}