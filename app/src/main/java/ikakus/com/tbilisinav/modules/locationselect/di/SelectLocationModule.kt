package ikakus.com.tbilisinav.modules.locationselect.di

import ikakus.com.tbilisinav.core.schedulers.SchedulerProvider
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationActionProcessorHolder
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewModel
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.applicationContext

class SelectLocationModule {
    val instance = applicationContext {
        factory {
            SelectLocationActionProcessorHolder(
                    SchedulerProvider())
        }
        viewModel { SelectLocationViewModel(get()) }
    }
}