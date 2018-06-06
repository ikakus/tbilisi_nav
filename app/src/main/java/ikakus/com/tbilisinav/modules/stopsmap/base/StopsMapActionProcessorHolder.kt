package ikakus.com.tbilisinav.modules.stopsmap.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopDataSource
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapAction.GetAllBusStopsInBoundsAction
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapResult.GetBusStopsInBoundResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class StopsMapActionProcessorHolder(
        private val busStopDataSource: BusStopDataSource,
        private val schedulerProvider: BaseSchedulerProvider
) {

    private val getStopsInBoundsProcessor =
            ObservableTransformer<GetAllBusStopsInBoundsAction, GetBusStopsInBoundResult> { actions ->
                actions.flatMap { action ->
                    busStopDataSource.getAllStopsInBounds(action.bounds)
                            .toObservable()
                            .map(GetBusStopsInBoundResult::Success)
                            .cast(GetBusStopsInBoundResult::class.java)
                            .onErrorReturn(GetBusStopsInBoundResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    internal var actionProcessor =
            ObservableTransformer<StopsMapAction, StopsMapResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(StopsMapAction.GetAllBusStopsInBoundsAction::class.java).compose(getStopsInBoundsProcessor)
                            .cast(StopsMapResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is StopsMapAction.GetAllBusStopsInBoundsAction }
                                            .flatMap { w ->
                                                Observable.error<StopsMapResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}