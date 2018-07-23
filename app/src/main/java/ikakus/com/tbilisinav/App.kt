package ikakus.com.tbilisinav

import android.app.Application
import ikakus.com.tbilisinav.core.di.DatabaseModule
import ikakus.com.tbilisinav.core.di.NetworkModule
import ikakus.com.tbilisinav.modules.busroute.routedetails.di.RouteDetailsModule
import ikakus.com.tbilisinav.modules.busroute.routelist.di.RouteListModule
import ikakus.com.tbilisinav.modules.locationselect.di.SelectLocationModule
import ikakus.com.tbilisinav.modules.navigation.bus.di.NavigationModule
import ikakus.com.tbilisinav.modules.nearbystops.di.NearbyModule
import ikakus.com.tbilisinav.modules.stopinfo.di.StopInfoModule
import ikakus.com.tbilisinav.modules.stopsmap.di.StopsMapModule
import org.koin.android.ext.android.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        setupDi()
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        } else {
            //Timber.plant(CrashReportingTree())
        }
    }

    private fun setupDi() {
        startKoin(this, listOf(
                NetworkModule().instance,
                DatabaseModule(this).instance,
                RouteDetailsModule().instance,
                RouteListModule().instance,
                StopsMapModule().instance,
                StopInfoModule().instance,
                NavigationModule().instance,
                SelectLocationModule().instance,
                NearbyModule().instance))
    }
}