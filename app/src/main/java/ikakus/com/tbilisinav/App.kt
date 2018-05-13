package ikakus.com.tbilisinav

import android.app.Application
import ikakus.com.tbilisinav.core.di.NetworkModule
import ikakus.com.tbilisinav.modules.busroute.routedetails.di.RouteDetailsModule
import ikakus.com.tbilisinav.modules.busroute.routelist.di.RouteListModule
import org.koin.android.ext.android.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDi()
    }

    private fun setupDi() {
        startKoin(this, listOf(
                NetworkModule().instance,
                RouteDetailsModule().instance,
                RouteListModule().instance))
    }
}