package ikakus.com.tbilisinav.data.database

import junit.framework.Assert
import org.junit.Test

class DBHelperTest {

    val sampleStr ="[\n" +
            "   {\n" +
            "      \"lat\":41.720345330581786,\n" +
            "      \"lng\":44.79881183950969,\n" +
            "      \"name\":\"სადგურის მოედანი (2)\",\n" +
            "      \"name_en\":\"Station Square (2)\",\n" +
            "      \"id\":\"799\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"lat\":41.71758232292782,\n" +
            "      \"lng\":44.791306845901524,\n" +
            "      \"name\":\"ბაქოს ქუჩა\",\n" +
            "      \"name_en\":\"Baku Street\",\n" +
            "      \"id\":\"801\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"lat\":41.71824821491409,\n" +
            "      \"lng\":44.790356189913105,\n" +
            "      \"name\":\"აღმაშენებლის გამზირი\",\n" +
            "      \"name_en\":\"Agmashenebeli Avenue\",\n" +
            "      \"id\":\"802\"\n" +
            "   },\n" +
            "   {\n" +
            "      \"lat\":41.71380199315995,\n" +
            "      \"lng\":44.78223389877563,\n" +
            "      \"name\":\"გმირთა მოედანი\",\n" +
            "      \"name_en\":\"Heroes Square (Saburtalo direction)\",\n" +
            "      \"id\":\"803\"\n" +
            "   }\n" +
            "]"

    val hepler = DBHelper()

    @Test
    fun parseDBJson() {
        val result = hepler.parseDBJson(sampleStr)
        Assert.assertEquals(true, result.isNotEmpty())
    }
}