package ikakus.com.tbilisinav.modules.navigation.bus.di

import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.data.source.navigation.NavigationRepository
import ikakus.com.tbilisinav.data.source.navigation.local.LocalNavigationDataSource
import ikakus.com.tbilisinav.data.source.navigation.remote.RemoteNavigationDataSource
import ikakus.com.tbilisinav.modules.navigation.bus.NavListener
import ikakus.com.tbilisinav.modules.navigation.bus.base.NavigationActionProcessorHolder
import ikakus.com.tbilisinav.modules.navigation.bus.base.NavigationViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class NavigationModule {
    val instance = applicationContext {
        factory {
            NavigationActionProcessorHolder(
                    NavigationRepository(
                            RemoteNavigationDataSource(get()),
                            LocalNavigationDataSource(get())),
                    ImmediateSchedulerProvider())
        }
        viewModel { NavigationViewModel(get()) }

        bean { NavListener() }
    }
}