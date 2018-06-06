package ikakus.com.tbilisinav.data.database

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ikakus.com.tbilisinav.data.database.models.MapBusStop
import java.io.ByteArrayOutputStream
import java.io.IOException

class DBHelper {

    inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)

    fun parseDBJson(jsonDBString: String): List<MapBusStop> {
        return Gson().fromJson<List<MapBusStop>>(jsonDBString)
                as ArrayList<MapBusStop>
    }

    fun readRawTextFile(ctx: Context, resId: Int): String? {
        val inputStream = ctx.resources.openRawResource(resId)
        val byteArrayOutputStream = ByteArrayOutputStream()

        var i: Int
        try {
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (e: IOException) {
            return null
        }

        return byteArrayOutputStream.toString()
    }
}