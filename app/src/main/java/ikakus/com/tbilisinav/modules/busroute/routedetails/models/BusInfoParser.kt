package ikakus.com.tbilisinav.modules.busroute.routedetails.models

import org.jsoup.Jsoup
import java.util.*

class BusInfoParser {
    fun parseBusStopInfo(strResponse: String) : List<BusInfoItem> {

        val document = Jsoup.parse(strResponse)
        // get all bus info elements
        val elements = document.select(".arrivalTimesScrol tr")

        // create BusInfo list with parsed data
        val buses = ArrayList<BusInfoItem>()
        elements.forEach {
            val busNum = it.child(0).text().toInt()
            val direction = it.child(1).text()
            val time = it.child(2).text().toInt()
            buses.add(BusInfoItem(busNum, direction, time))
        }
        return buses
    }
}