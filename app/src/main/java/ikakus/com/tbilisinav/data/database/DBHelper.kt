package ikakus.com.tbilisinav.data.database

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ikakus.com.tbilisinav.data.database.models.MapBusStop

class DBHelper {

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

    fun parseDBJson(jsonDBString: String): List<MapBusStop> {
        return Gson().fromJson<List<MapBusStop>>(jsonDBString)
                as ArrayList<MapBusStop>
    }

}