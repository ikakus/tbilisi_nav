package ikakus.com.tbilisinav.core.di

import android.arch.persistence.room.Room
import android.content.Context
import ikakus.com.tbilisinav.R
import ikakus.com.tbilisinav.core.schedulers.SchedulerProvider
import ikakus.com.tbilisinav.data.database.AppDatabase
import ikakus.com.tbilisinav.data.database.DBHelper
import ikakus.com.tbilisinav.utils.FileHelper
import io.reactivex.functions.Consumer
import org.koin.dsl.module.applicationContext
import timber.log.Timber

class DatabaseModule(context: Context) {
    val instance = applicationContext {

        val db = Room.databaseBuilder(context,
                AppDatabase::class.java, "tbilisi_nav-db").build()

        setupDatabase(context, db = db)
        bean {
            db.busStopDao()
        }
    }

    private fun setupDatabase(context: Context, db: AppDatabase) {
        val dao = db.busStopDao()
        val fileHelper = FileHelper()
        dao.getAll()
                .subscribeOn(SchedulerProvider.io())
                .subscribe(Consumer {
                    if (it.isEmpty()) {
                        val helper = DBHelper()
                        Timber.d("Fetching Room")
                        val json = fileHelper.readRawTextFile(context, R.raw.db)
                        json?.let {
                            val list = helper.parseDBJson(it)
                            dao.insertAll(list)
                            Timber.d("Room fetched")
                        }
                    } else {
                        Timber.d("Room is present")
                    }
                })
    }
}