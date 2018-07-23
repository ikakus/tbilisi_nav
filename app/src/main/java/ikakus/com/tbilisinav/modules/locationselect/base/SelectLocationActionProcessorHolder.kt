package ikakus.com.tbilisinav.modules.locationselect.base

import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import io.reactivex.Single


class SelectLocationActionProcessorHolder(private val schedulerProvider: BaseSchedulerProvider) {

    private val selectStartProcessor =
            ObservableTransformer<SelectLocationAction.SelectStartLocationAction, SelectLocationResult> { actions ->
                actions.flatMap { action ->
                    Single.just(action.location)
                            .toObservable()
                            .map { SelectLocationResult.SelectStartResult.Success(it) }
                            .cast(SelectLocationResult.SelectStartResult::class.java)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val selectEndProcessor =
            ObservableTransformer<SelectLocationAction.SelectEndLocationAction, SelectLocationResult> { actions ->
                actions.flatMap { action ->
                    Single.just(action.location)
                            .toObservable()
                            .map { SelectLocationResult.SelectEndResult.Success(it) }
                            .cast(SelectLocationResult.SelectEndResult::class.java)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }

    private val clearLocationsProcessor =
            ObservableTransformer<SelectLocationAction.ClearLocationsAction, SelectLocationResult> { actions ->
                actions.flatMap { action ->
                    Single.just(action)
                            .toObservable()
                            .map { SelectLocationResult.ClearLocationsResult.Success() }
                            .cast(SelectLocationResult.ClearLocationsResult::class.java)
                            .subscribeOn(schedulerProvider.io())
                            .observeOn(schedulerProvider.ui())
                }
            }


    internal var actionProcessor =
            ObservableTransformer<SelectLocationAction, SelectLocationResult> { actions ->
                actions.publish { shared ->
                    Observable.merge<SelectLocationResult>(
                            shared.ofType(SelectLocationAction.SelectStartLocationAction::class.java).compose(selectStartProcessor),
                            shared.ofType(SelectLocationAction.ClearLocationsAction::class.java).compose(clearLocationsProcessor),
                            shared.ofType(SelectLocationAction.SelectEndLocationAction::class.java).compose(selectEndProcessor))
                            .mergeWith(
                                    shared.filter { v ->
                                        v !is SelectLocationAction.SelectStartLocationAction &&
                                                v !is SelectLocationAction.SelectEndLocationAction &&
                                                v !is SelectLocationAction.ClearLocationsAction
                                    }
                                            .flatMap { w ->
                                                Observable.error<SelectLocationResult>(
                                                        IllegalArgumentException("Unknown Action type: " + w))
                                            })
                }
            }
}