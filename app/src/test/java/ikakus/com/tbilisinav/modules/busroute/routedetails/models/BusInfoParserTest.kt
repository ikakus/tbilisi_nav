package ikakus.com.tbilisinav.modules.busroute.routedetails.models

import org.junit.Assert.assertEquals
import org.junit.Test

class BusInfoParserTest {

    val parser = BusInfoParser()

    val data = "<table cellspacing='0' cellpadding='0' class='arrivalTimesTable'>\n" +
            "    <tr>\n" +
            "        <td class='arrivalTableRouteNumberHeader' title='Route Number'>Route</td>\n" +
            "        <td class='arrivalTableStopNameHeader' title='Route Diretion'>Direction</td>\n" +
            "        <td class='arrivalTableArrivalTimeHeader' title='Bus arrival time'>min.</td>\n" +
            "    </tr>\n" +
            "    <tr>\n" +
            "        <td colspan='3'>\n" +
            "            <div class='arrivalTimesScrol'>\n" +
            "                <table cellspacing='0' cellpadding='0' class='arrivalTimesInnerTable'>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>92</td>\n" +
            "                        <td class='arrivalTableStopName' title='Station Square'>Station Square</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>4</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>54</td>\n" +
            "                        <td class='arrivalTableStopName' title='Didi Dighomi IV M/D'>Didi Dighomi IV M/D</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>5</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>88</td>\n" +
            "                        <td class='arrivalTableStopName' title='Baratashvili Str.'>Baratashvili Str.</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>5</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>21</td>\n" +
            "                        <td class='arrivalTableStopName' title='Didi Dighomi IV M/D'>Didi Dighomi IV M/D</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>6</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>150</td>\n" +
            "                        <td class='arrivalTableStopName' title='Baratashvili Str.'>Baratashvili Str.</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>6</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>13</td>\n" +
            "                        <td class='arrivalTableStopName' title='State University  H.B'>State University  H.B</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>7</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>140</td>\n" +
            "                        <td class='arrivalTableStopName' title='Baratashvili Str.'>Baratashvili Str.</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>8</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>24</td>\n" +
            "                        <td class='arrivalTableStopName' title='Gldani VII - VIII M/D'>Gldani VII - VIII M/D</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>10</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>49</td>\n" +
            "                        <td class='arrivalTableStopName' title='Station Square'>Station Square</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>14</td>\n" +
            "                    </tr>\n" +
            "                    <tr>\n" +
            "                        <td class='arrivalTableRouteNumber'>5</td>\n" +
            "                        <td class='arrivalTableStopName' title='Tskneti'>Tskneti</td>\n" +
            "                        <td class='arrivalTableArrivalTime'>45</td>\n" +
            "                    </tr>\n" +
            "                </table>\n" +
            "            </div>\n" +
            "        </td>\n" +
            "    </tr>\n" +
            "</table>"

    @Test
    fun parseBusStopInfo() {
        val model = parser.parseBusStopInfo(data)
        assertEquals(true, model.isNotEmpty())
    }
}