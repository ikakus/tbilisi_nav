package ikakus.com.tbilisinav.utils

import android.content.Context
import java.io.ByteArrayOutputStream
import java.io.IOException

class FileHelper {
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