package ikakus.com.tbilisinav.data.source.navigation.models
import com.google.gson.annotations.SerializedName

data class BusNavigationResponseModel(
    @SerializedName("requestParameters") val requestParameters: RequestParameters,
    @SerializedName("plan") val plan: Plan
)

data class RequestParameters(
    @SerializedName("optimize") val optimize: String, // QUICK
    @SerializedName("time") val time: String, // 10:57
    @SerializedName("wheelchair") val wheelchair: String, // false
    @SerializedName("maxWalkDistance") val maxWalkDistance: String, // 500.0
    @SerializedName("routerId") val routerId: String,
    @SerializedName("fromPlace") val fromPlace: String, // 41.723157,44.721624
    @SerializedName("toPlace") val toPlace: String, // 41.725975,44.769346
    @SerializedName("date") val date: String, // 2018-06-07
    @SerializedName("mode") val mode: String, // TraverseMode (WALK, TRAM, SUBWAY, RAIL, BUS, FERRY, CABLE_CAR, GONDOLA, FUNICULAR, TRANSIT, TRAINISH, BUSISH)
    @SerializedName("numItineraries") val numItineraries: String // 3
)

data class Plan(
        @SerializedName("date") val date: Long, // 1528354620000
        @SerializedName("from") val from: LegPoint,
        @SerializedName("to") val to: LegPoint,
        @SerializedName("itineraries") val itineraries: List<Itinerary>
)

data class Itinerary(
    @SerializedName("duration") val duration: Int, // 1395000
    @SerializedName("startTime") val startTime: Long, // 1528356835000
    @SerializedName("endTime") val endTime: Long, // 1528358230000
    @SerializedName("walkTime") val walkTime: Int, // 313
    @SerializedName("transitTime") val transitTime: Int, // 840
    @SerializedName("waitingTime") val waitingTime: Int, // 242
    @SerializedName("walkDistance") val walkDistance: Double, // 390.5988318207534
    @SerializedName("elevationLost") val elevationLost: Int, // 0
    @SerializedName("elevationGained") val elevationGained: Int, // 0
    @SerializedName("transfers") val transfers: Int, // 0
    @SerializedName("fare") val fare: Any, // null
    @SerializedName("legs") val legs: List<Leg>,
    @SerializedName("tooSloped") val tooSloped: Boolean // false
)

data class Leg(
        @SerializedName("startTime") val startTime: Long, // 1528358101000
        @SerializedName("endTime") val endTime: Long, // 1528358230000
        @SerializedName("distance") val distance: Double, // 156.3898807322188
        @SerializedName("mode") val mode: Mode, // WALK
        @SerializedName("route") val route: String,
        @SerializedName("agencyName") val agencyName: Any, // null
        @SerializedName("agencyUrl") val agencyUrl: Any, // null
        @SerializedName("routeColor") val routeColor: Any, // null
        @SerializedName("routeTextColor") val routeTextColor: Any, // null
        @SerializedName("interlineWithPreviousLeg") val interlineWithPreviousLeg: Any, // null
        @SerializedName("tripShortName") val tripShortName: Any, // null
        @SerializedName("headsign") val headsign: Any, // null
        @SerializedName("agencyId") val agencyId: Any, // null
        @SerializedName("tripId") val tripId: Any, // null
        @SerializedName("from") val from: LegPoint,
        @SerializedName("to") val to: LegPoint,
        @SerializedName("legGeometry") val legGeometry: LegGeometry,
        @SerializedName("routeShortName") val routeShortName: Any, // null
        @SerializedName("routeLongName") val routeLongName: Any, // null
        @SerializedName("boardRule") val boardRule: Any, // null
        @SerializedName("alightRule") val alightRule: Any, // null
        @SerializedName("duration") val duration: Int, // 129000
        @SerializedName("bogusNonTransitLeg") val bogusNonTransitLeg: Boolean, // false
        @SerializedName("steps") val steps: List<Step>,
        @SerializedName("intermediateStops") val intermediateStops: List<Any>
)

data class Step(
    @SerializedName("distance") val distance: Double, // 75.30345375203329
    @SerializedName("relativeDirection") val relativeDirection: String, // SLIGHTLY_LEFT
    @SerializedName("streetName") val streetName: String, // ბილიკი
    @SerializedName("absoluteDirection") val absoluteDirection: String, // SOUTH
    @SerializedName("exit") val exit: Any, // null
    @SerializedName("stayOn") val stayOn: Boolean, // false
    @SerializedName("bogusName") val bogusName: Boolean, // true
    @SerializedName("lon") val lon: Double, // 44.7689573
    @SerializedName("lat") val lat: Double, // 41.7265246
    @SerializedName("elevation") val elevation: String
)

data class LegGeometry(
    @SerializedName("points") val points: String, // uyt}Fa{fpGv@y@HKZC^LRIL?N@BYNa@VY
    @SerializedName("levels") val levels: Any, // null
    @SerializedName("length") val length: Int // 11
)

data class LegPoint(
    @SerializedName("name") val name: String, // პეკინის გამზირი (Pekin Ave)
    @SerializedName("stopId") val stopId: Stop?, // null
    @SerializedName("stopCode") val stopCode: Any, // null
    @SerializedName("lon") val lon: Double, // 44.768655804791926
    @SerializedName("lat") val lat: Double, // 41.72715703183146
    @SerializedName("arrival") val arrival: Any, // null
    @SerializedName("departure") val departure: Any, // null
    @SerializedName("orig") val orig: Any, // null
    @SerializedName("zoneId") val zoneId: Any, // null
    @SerializedName("geometry") val geometry: String // {"type": "Point", "coordinates": [44.768655804791926,41.72715703183146]}
)

data class Stop(
        @SerializedName("agencyId") val agencyId: String, // "TTC"
        @SerializedName("id") val stopId: String // 963 - metro_1_10
)

enum class Mode {
    @SerializedName("WALK")
    WALK,
    @SerializedName("BUS")
    BUS,
    @SerializedName("SUBWAY")
    SUBWAY,
}