package ikakus.com.tbilisinav.utils

import android.content.Context
import ikakus.com.tbilisinav.R
import java.util.concurrent.TimeUnit

class TimeHelper(val context: Context) {

    fun getStringFromMillis(millis: Long):String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        if(minutes < 1){
            return context.resources.getString(R.string.minute, 1)
        }

        if (minutes > 60){
            val hour = TimeUnit.MILLISECONDS.toHours(millis)
            if(minutes - 60 > 0){
                val leftMins = minutes - 60
                return context.resources.getString(R.string.hour, hour) +
                        context.resources.getString(R.string.minute, leftMins)
            }

            return context.resources.getString(R.string.hour, hour)
        }
        return context.resources.getString(R.string.minute, minutes)
    }

    fun getStringFromSeconds(millis: Long):String {
        val minutes = TimeUnit.SECONDS.toMinutes(millis)
        if(minutes < 1){
            return context.resources.getString(R.string.minute, 1)
        }

        if (minutes > 60){
            val hour = TimeUnit.SECONDS.toHours(millis)
            if(minutes - 60 > 0){
                val leftMins = minutes - 60
                return context.resources.getString(R.string.hour, hour) +
                        context.resources.getString(R.string.minute, leftMins)
            }

            return context.resources.getString(R.string.hour, hour)
        }
        return context.resources.getString(R.string.minute, minutes)
    }

}