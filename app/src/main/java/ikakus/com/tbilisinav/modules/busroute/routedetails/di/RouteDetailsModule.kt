package ikakus.com.tbilisinav.modules.busroute.routedetails.di

import ikakus.com.tbilisinav.core.schedulers.SchedulerProvider
import ikakus.com.tbilisinav.data.source.busstops.BusStopRepository
import ikakus.com.tbilisinav.data.source.busstops.local.LocalBusStopDataSource
import ikakus.com.tbilisinav.data.source.busstops.remote.RemoteBusStopDataSource
import ikakus.com.tbilisinav.data.source.route.RouteRepository
import ikakus.com.tbilisinav.data.source.route.local.LocalRouteDataSource
import ikakus.com.tbilisinav.data.source.route.remote.RemoteRouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsActionProcessorHolder
import ikakus.com.tbilisinav.modules.busroute.routedetails.base.RouteDetailsViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class RouteDetailsModule {
    val instance = applicationContext {
        factory {
            RouteDetailsActionProcessorHolder(
                    RouteRepository(
                            RemoteRouteDataSource(get()),
                            LocalRouteDataSource()),
                    BusStopRepository(
                            RemoteBusStopDataSource(get()),
                            LocalBusStopDataSource(get())),
                    SchedulerProvider())
        }
        viewModel { RouteDetailsViewModel(get()) }
    }
}