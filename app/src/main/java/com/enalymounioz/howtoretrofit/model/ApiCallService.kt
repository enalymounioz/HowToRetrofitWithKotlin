package com.enalymounioz.howtoretrofit.model

import android.content.Context
import com.enalymounioz.howtoretrofit.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object ApiCallService {

    private val BASE_URL = "https://test-remote-health-monitoring.wings-ict-solutions.dev/"
    private var api: ApiCall? = null

    private fun getApi(context: Context): ApiCall {
        if (api == null) {
            val okHttp2Client = OkHttpClient.Builder()
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            if (BuildConfig.DEBUG) {
                okHttp2Client.addInterceptor(logging)
        }
            val cacheSize = 5*1024*1024L
            val cache = Cache(context.cacheDir, cacheSize)
            okHttp2Client.cache(cache)

            api = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttp2Client.build())
                .build()
                .create(ApiCall::class.java)
        }
        return api!!
    }
    private fun getApiRx()=
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiCall::class.java)

    fun callRx() = getApiRx()

    fun call(context: Context) =
        getApi(context).callGet()
    //getApi(context).callPost()
    //getApi(context).callQueryStatic()
    //getApi(context).callQueryDynamic("James", 27)
    //getApi(context).callQueryDynamic(null, 27)
    //getApi(context).callQueryMultiple(hashMapOf(Pair("a", "sfb"), Pair("b", "value2")))
    //getApi(context).callUrlBypass()
    //getApi(context).callUrlDynamic("https://example.com/user")
    //getApi(context).callUrlPath("info")
    //getApi(context).callUrlPath("getApi(context)Call")
    //getApi(context).callPost(User("system_admin", "healthmonitoring2021"))
    //getApi(context).callFormData("James", "Bond")
    //getApi(context).callFormData(hashMapOf(Pair("name", "Alex"), Pair("lastname", "Smith")))
    //getApi(context).callHeadersStatic()
    //getApi(context).callHeadersDynamic("Michael")
    //getApi(context).callHeaderMultiple(hashMapOf(Pair("user-agent", "abc"), Pair("username", "John")))
}