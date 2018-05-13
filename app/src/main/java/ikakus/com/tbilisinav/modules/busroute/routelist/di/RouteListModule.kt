package ikakus.com.tbilisinav.modules.busroute.routelist.di

import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.source.RouteRepository
import ikakus.com.tbilisinav.data.source.local.LocalRouteDataSource
import ikakus.com.tbilisinav.data.source.remote.RemoteRouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routelist.base.RouteListActionProcessorHolder
import ikakus.com.tbilisinav.modules.busroute.routelist.base.RouteListViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class RouteListModule {
    val instance = applicationContext {
        factory {
            RouteListActionProcessorHolder(
                    RouteRepository(
                            RemoteRouteDataSource(get()),
                            LocalRouteDataSource()),
                    ImmediateSchedulerProvider())
        }
        viewModel { RouteListViewModel(get()) }
    }
}