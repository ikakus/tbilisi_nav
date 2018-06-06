package ikakus.com.tbilisinav.modules.stopsmap.di

import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopRepository
import ikakus.com.tbilisinav.data.source.busstops.local.LocalBusStopDataSource
import ikakus.com.tbilisinav.data.source.busstops.remote.RemoteBusStopDataSource
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapActionProcessorHolder
import ikakus.com.tbilisinav.modules.stopsmap.base.StopsMapViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class StopsMapModule {
    val instance = applicationContext {
        factory {
            StopsMapActionProcessorHolder(
                    BusStopRepository(
                            RemoteBusStopDataSource(get()),
                            LocalBusStopDataSource(get())),
                    ImmediateSchedulerProvider())
        }
        viewModel { StopsMapViewModel(get()) }
    }
}