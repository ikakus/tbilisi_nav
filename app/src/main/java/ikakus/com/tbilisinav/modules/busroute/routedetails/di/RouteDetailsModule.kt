package ikakus.com.tbilisinav.modules.busroute.routedetails.di

import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.source.RouteRepository
import ikakus.com.tbilisinav.data.source.local.LocalRouteDataSource
import ikakus.com.tbilisinav.data.source.remote.RemoteRouteDataSource
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
                    ImmediateSchedulerProvider())
        }
        viewModel { RouteDetailsViewModel(get()) }
    }
}