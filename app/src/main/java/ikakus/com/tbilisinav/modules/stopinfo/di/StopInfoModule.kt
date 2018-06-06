package ikakus.com.tbilisinav.modules.stopinfo.di

import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopRepository
import ikakus.com.tbilisinav.data.source.busstops.local.LocalBusStopDataSource
import ikakus.com.tbilisinav.data.source.busstops.remote.RemoteBusStopDataSource
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoActionProcessorHolder
import ikakus.com.tbilisinav.modules.stopinfo.base.StopInfoViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class StopInfoModule {
    val instance = applicationContext {
        factory {
            StopInfoActionProcessorHolder(
                    BusStopRepository(
                            RemoteBusStopDataSource(get()),
                            LocalBusStopDataSource(get())),
                    ImmediateSchedulerProvider())
        }
        viewModel { StopInfoViewModel(get()) }
    }
}