package ikakus.com.tbilisinav.selectlocation

import com.google.android.gms.maps.model.LatLng
import ikakus.com.tbilisinav.core.schedulers.BaseSchedulerProvider
import ikakus.com.tbilisinav.core.schedulers.ImmediateSchedulerProvider
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationActionProcessorHolder
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationIntent
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewModel
import ikakus.com.tbilisinav.modules.locationselect.base.SelectLocationViewState
import io.reactivex.Observable
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations

class SelectLocationViewModelTest{

    private lateinit var schedulerProvider: BaseSchedulerProvider
    private lateinit var selectLocationViewModel: SelectLocationViewModel
    private lateinit var testObserver: TestObserver<SelectLocationViewState>

    @Before
    fun setup(){
        MockitoAnnotations.initMocks(this)
        schedulerProvider = ImmediateSchedulerProvider()
        selectLocationViewModel = SelectLocationViewModel(
                SelectLocationActionProcessorHolder(schedulerProvider)
        )

        testObserver = selectLocationViewModel.states().test()
    }

    @Test
    fun setStartLocation(){
        selectLocationViewModel.processIntents(Observable.just(
                SelectLocationIntent.SelectStartLocationAction(LatLng(1.0,1.0))
        ))

        testObserver.assertValueAt(1){(startLocation, _) ->
            startLocation != null
        }
    }

    @Test
    fun setEndLocation(){
        selectLocationViewModel.processIntents(Observable.just(
                SelectLocationIntent.SelectEndLocationAction(LatLng(1.0,1.0))
        ))

        testObserver.assertValueAt(1){(_, endLocation) ->
            endLocation != null
        }
    }

}