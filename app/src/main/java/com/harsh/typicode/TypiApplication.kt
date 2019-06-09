package com.harsh.typicode

import android.app.Application
import androidx.room.Room
import com.harsh.typicode.data.AlbumsRepository
import com.harsh.typicode.data.local.dao.AlbumsDao
import com.harsh.typicode.data.local.database.DatabaseManager
import com.harsh.typicode.data.remote.service.AlbumsService
import com.harsh.typicode.data.remote.task.AlbumsTaskFactory
import com.harsh.typicode.data.remote.task.AlbumsTaskManager
import com.harsh.typicode.utils.IoScheduler
import com.harsh.typicode.utils.MainScheduler
import com.harsh.typicode.utils.NetworkScheduler
import com.harsh.typicode.utils.Scheduler
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Harsh Mittal on 2019-06-07.
 */
class TypiApplication : Application() {
    private lateinit var databaseManager: DatabaseManager

    lateinit var albumsDao: AlbumsDao
        private set

    lateinit var retrofit: Retrofit
        private set

    lateinit var albumsService: AlbumsService
        private set

    lateinit var mainScheduler: Scheduler
        private set

    lateinit var networkScheduler: Scheduler
        private set

    lateinit var ioScheduler: Scheduler
        private set

    lateinit var albumsTaskFactory: AlbumsTaskFactory
        private set

    lateinit var albumsTaskManager: AlbumsTaskManager
        private set

    lateinit var albumsRepository: AlbumsRepository
        private set

    private val REQUEST_TIMEOUT = 60
    private val okHttpClient: OkHttpClient by lazy { initOkHttp() }



    override fun onCreate() {
        super.onCreate()
        databaseManager = Room.databaseBuilder(this, DatabaseManager::class.java, "database")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()

        albumsDao = databaseManager.albumsDao
        retrofit = Retrofit.Builder()
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        albumsService = retrofit.create(AlbumsService::class.java)

        mainScheduler = MainScheduler()
        ioScheduler = IoScheduler()
        networkScheduler = NetworkScheduler()

        albumsTaskManager = AlbumsTaskManager()
        albumsTaskFactory =
            AlbumsTaskFactory(albumsTaskManager, albumsService, albumsDao, networkScheduler, ioScheduler)

        albumsRepository = AlbumsRepository(albumsDao, albumsTaskManager, albumsTaskFactory, mainScheduler)
    }

    private fun initOkHttp(): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        httpClient.addInterceptor(interceptor)

        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                .addHeader("Content-Type", "application/json")

            val request = requestBuilder.build()
            chain.proceed(request)
        }

        return httpClient.build()
    }
}
