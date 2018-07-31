package ikakus.com.tbilisinav.modules.busroute.routedetails

import ikakus.com.tbilisinav.utils.GMapHelper
import junit.framework.Assert.assertEquals
import org.junit.Test

class GMapHelperTest {

    private val shape = "44.804151:41.696604,44.803665:41.696543,44.803359:41.696505,44.803187:41.696447"
    private val mapHepler = GMapHelper()

    @Test
    fun decodeShapeTest_true() {
        val decoded = mapHepler.decodeShape(shape)
        assertEquals(true, decoded.isNotEmpty())
    }

    @Test
    fun decodeShapeTest_false() {
        val decoded = mapHepler.decodeShape("")
        assertEquals(false, decoded.isNotEmpty())
    }
}