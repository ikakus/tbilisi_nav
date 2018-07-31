package ikakus.com.tbilisinav.modules.nearbystops.di

import ikakus.com.tbilisinav.core.schedulers.SchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopRepository
import ikakus.com.tbilisinav.data.source.busstops.local.LocalBusStopDataSource
import ikakus.com.tbilisinav.data.source.busstops.remote.RemoteBusStopDataSource
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsActionProcessorHolder
import ikakus.com.tbilisinav.modules.nearbystops.base.NearbyStopsViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class NearbyModule {
    val instance = applicationContext {
        factory {
            NearbyStopsActionProcessorHolder(
                    BusStopRepository(
                            RemoteBusStopDataSource(get()),
                            LocalBusStopDataSource(get())),
                    SchedulerProvider())
        }
        viewModel { NearbyStopsViewModel(get()) }
    }
}