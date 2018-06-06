package ikakus.com.tbilisinav.modules.stopinfo.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopDataSource
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoAction.GetStopInfoAction
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoResult.GetStopInfoResult
import io.reactivex.Observable
import io.reactivex.ObservableTransformer

class StopInfoActionProcessorHolder(
        private val busStopDataSource: BusStopDataSource,
        private val schedulerProvider: BaseSchedulerProvider
) {

    private val getStopsInBoundsProcessor =
            ObservableTransformer<GetStopInfoAction, GetStopInfoResult> { actions ->
                actions.flatMap { action ->
                    busStopDataSource.getBusStopInfo(action.id)
                            .toObservable()
                            .map(GetStopInfoResult::Success)
                            .cast(GetStopInfoResult::class.java)
                            .onErrorReturn(GetStopInfoResult::Failure)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                            .startWith(StopInfoResult.GetStopInfoResult.InFlight)
                }
            }

    internal var actionProcessor =
            ObservableTransformer<StopInfoAction, StopInfoResult> { actions ->
                actions.publish { shared ->
                    shared.ofType(StopInfoAction.GetStopInfoAction::class.java).compose(getStopsInBoundsProcessor)
                            .cast(StopInfoResult::class.java)
                            .mergeWith(
                                    shared.filter { v -> v !is StopInfoAction.GetStopInfoAction }
                                            .flatMap { w ->
                                                Observable.error<StopInfoResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}