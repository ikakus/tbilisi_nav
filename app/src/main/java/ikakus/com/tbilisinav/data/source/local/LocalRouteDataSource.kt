package ikakus.com.tbilisinav.data.source.local

import ikakus.com.tbilisinav.data.source.RouteDataSource
import ikakus.com.tbilisinav.modules.busroute.routedetails.models.RouteModel
import io.reactivex.Single

class LocalRouteDataSource(): RouteDataSource {
    // TODO add local caching with Room
    override fun getRouteDetails(busId: Int): Single<RouteModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getRouteList(): Single<List<Int>> {
        val busRoutesList = ArrayList<Int>()
        // TOD move list to resources
        for(i in 1..56){
            busRoutesList.add(i)
        }

        busRoutesList.add(59)
        busRoutesList.add(60)
        busRoutesList.add(61)
        busRoutesList.add(62)
        busRoutesList.add(65)
        busRoutesList.add(66)
        busRoutesList.add(68)
        busRoutesList.add(69)
        busRoutesList.add(70)
        busRoutesList.add(71)
        busRoutesList.add(72)
        busRoutesList.add(73)
        busRoutesList.add(75)
        busRoutesList.add(77)
        busRoutesList.add(78)
        busRoutesList.add(79)
        busRoutesList.add(80)
        busRoutesList.add(82)
        busRoutesList.add(84)
        busRoutesList.add(85)
        busRoutesList.add(86)
        busRoutesList.add(87)
        busRoutesList.add(88)
        busRoutesList.add(90)
        busRoutesList.add(91)
        busRoutesList.add(92)
        busRoutesList.add(94)
        busRoutesList.add(95)
        busRoutesList.add(99)
        busRoutesList.add(101)
        busRoutesList.add(102)
        busRoutesList.add(103)
        busRoutesList.add(104)
        busRoutesList.add(106)
        busRoutesList.add(107)
        busRoutesList.add(108)
        busRoutesList.add(109)
        busRoutesList.add(110)
        busRoutesList.add(111)
        busRoutesList.add(112)
        busRoutesList.add(121)
        busRoutesList.add(122)
        busRoutesList.add(124)
        busRoutesList.add(140)
        busRoutesList.add(150)

        return Single.just(busRoutesList)
    }
}