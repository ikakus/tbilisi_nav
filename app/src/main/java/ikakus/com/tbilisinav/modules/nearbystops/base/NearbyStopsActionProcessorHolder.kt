package ikakus.com.tbilisinav.modules.nearbystops.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopDataSource
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsAction.GetNearbyBusStopsAction
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class NearbyStopsActionProcessorHolder(
        private val busStopDataSource: BusStopDataSource,
        private val schedulerProvider: BaseSchedulerProvider
) {

    private val getNearbyStopsProcessor =
            ObservableTransformer<GetNearbyBusStopsAction, NearbyStopsResult.GetNearbyBusStopsResult> { actions ->
                actions.flatMap { action ->
                    busStopDataSource.getNearby(action.location)
                            .toObservable()
                            .map(NearbyStopsResult.GetNearbyBusStopsResult::Success)
                            .cast(NearbyStopsResult.GetNearbyBusStopsResult::class.java)
                            .onErrorReturn(NearbyStopsResult.GetNearbyBusStopsResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(NearbyStopsResult.GetNearbyBusStopsResult.InFlight)
                }
            }


//    internal var actionProcessor =
//            ObservableTransformer<NearbyStopsAction, NearbyStopsResult> { actions ->
//                actions.publish({ shared ->
//                    Observable.merge<NearbyStopsResult>(
//                            shared.ofType(GetNearbyBusStopsAction::class.java).compose(getNearbyStopsProcessor),
//                            shared.ofType(NearbyStopsAction.GetAllBusStopsInBoundsAction::class.java).compose(getStopsInBoundsProcessor))
//                            .mergeWith(
//                                    // Error for not implemented actions
//                                    shared.filter({ v ->
//                                        (v !is GetAllBusStopsAction
//                                                && v !is GetNearbyBusStopsAction
//                                                && v !is NearbyStopsAction.GetAllBusStopsInBoundsAction
//                                                )
//                                    })
//                                            .flatMap({ w ->
//                                                Observable.error<NearbyStopsResult>(
//                                                        IllegalArgumentException("Unknown Action type: " + w))
//                                            }))
//                })
//            }

    internal var actionProcessor =
            ObservableTransformer<NearbyStopsAction, NearbyStopsResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(NearbyStopsAction.GetNearbyBusStopsAction::class.java).compose(getNearbyStopsProcessor)
                            .cast(NearbyStopsResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is NearbyStopsAction.GetNearbyBusStopsAction }
                                            .flatMap { w ->
                                                Observable.error<NearbyStopsResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}