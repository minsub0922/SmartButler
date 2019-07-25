package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import okhttp3.CookieJar
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.internal.JavaNetCookieJar
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookieStore
import java.util.concurrent.TimeUnit


var LIST_API_BASE_URL = "http://112.169.29.116:3003"

var UTAS_API_BASE_URL = "http://112.169.29.116:8081"

lateinit var listClient: OkHttpClient

lateinit var listRetrofit: Retrofit

lateinit var utasRetrofit: Retrofit

val listNetworkInterface: NetworkRouters by lazy {  listRetrofit.create(NetworkRouters::class.java) }
val utasNetworkInterface: NetworkRouters by lazy {  utasRetrofit.create(NetworkRouters::class.java)}

fun listNetworkInit() {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    var cookieHandler: CookieHandler = CookieManager()

    listClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(DecodeInterceptor())
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)
            .cookieJar(JavaNetCookieJar(cookieHandler))
            .build()

    listRetrofit = Retrofit.Builder()
            .baseUrl(LIST_API_BASE_URL)
            .client(listClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            //.addConverterFactory(ScalarsConverterFactory.create())  //for string body
            .build()

    utasRetrofit = Retrofit.Builder()
            .baseUrl(UTAS_API_BASE_URL)
            .client(listClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())  //for string body
            .build()

}

fun getListNetworkInstance() = listNetworkInterface
fun getUtasNetworkInstance() = utasNetworkInterface
