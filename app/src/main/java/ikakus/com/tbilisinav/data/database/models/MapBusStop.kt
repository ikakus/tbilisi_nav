package ikakus.com.tbilisinav.data.database.models

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity
data class MapBusStop(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lng: Double,
        @SerializedName("name") val name: String,
        @SerializedName("name_en") val nameEn: String?,
        @PrimaryKey
        @SerializedName("id") val id: Int
)