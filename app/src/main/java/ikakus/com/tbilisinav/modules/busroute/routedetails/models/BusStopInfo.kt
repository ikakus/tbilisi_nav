package ikakus.com.tbilisinav.modules.busroute.routedetails.models

data class BusStopInfoModel(val items: List<BusInfoItem> = ArrayList())

data class BusInfoItem(val number: Int,
                       val text: String,
                       val time: Int)
