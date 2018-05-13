package ikakus.com.tbilisinav.modules.busroute.routedetails.models

import com.google.gson.annotations.SerializedName


data class RouteModel(
        @SerializedName("Color") val color: String = "",
        @SerializedName("RouteNumber") val routeNumber: Int = -1,
        @SerializedName("RouteStops") val routeStops: List<RouteStop> = emptyList(),
        @SerializedName("Shape") val shape: String = "",
        @SerializedName("Type") val type: String = ""
)

data class RouteStop(
        @SerializedName("Forward") val forward: Boolean,
        @SerializedName("HasBoard") val hasBoard: Boolean,
        @SerializedName("Lat") val lat: Double,
        @SerializedName("Lon") val lng: Double,
        @SerializedName("Name") val name: String,
        @SerializedName("Sequence") val sequence: Int,
        @SerializedName("StopId") val stopId: Int,
        @SerializedName("Virtual") val virtual: Boolean
)