package ikakus.com.tbilisinav.core.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.applicationContext
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class NetworkModule {
    private val BASE_URL = "http://transit.ttc.com.ge/pts-portal-services/"

    val instance = applicationContext {
        bean {
            val cacheSize = 10 * 1024 * 1024
            Cache(this.androidApplication().cacheDir, cacheSize.toLong())
        }

        bean {
            val gsonBuilder = GsonBuilder()
            gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss")
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            gsonBuilder.setLenient()
            gsonBuilder.create()
        }

        bean {
            val client = OkHttpClient.Builder()
            client.cache(get())
            client.readTimeout(5, TimeUnit.MINUTES)
            client.build()
        }

        bean {
            val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(get()))
                    .addCallAdapterFactory(rxAdapter)
                    .baseUrl(BASE_URL)
                    .client(get())
                    .build()
        }
    }
}