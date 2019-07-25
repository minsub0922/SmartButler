package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


val API_BASE_URL = "http://112.169.29.116:8080"
val CCTV_BASE_URL = "http://172.16.16.87:10005"

lateinit var client: OkHttpClient

lateinit var retrofit: Retrofit
lateinit var retrofitForJson: Retrofit
lateinit var retrofitForCCTV: Retrofit

val networkInterface: NetworkRouters by lazy {  retrofit.create(NetworkRouters::class.java) }
val networkInterfaceForJsonTypes: NetworkRouters by lazy {  retrofitForJson.create(NetworkRouters::class.java) }
val cctvNetworkInterface: NetworkRouters by lazy {  retrofitForCCTV.create(NetworkRouters::class.java) }

fun networkInit() {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(DecodeInterceptor())
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            //.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .addConverterFactory(ScalarsConverterFactory.create())  //for string body
            .build()

    retrofitForJson = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    retrofitForCCTV = Retrofit.Builder()
            .baseUrl(CCTV_BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()
}

fun getNetworkInstance() = networkInterface

fun getNetworkInstanceForJson() = networkInterfaceForJsonTypes

fun getCCTVNetworkInstance() = cctvNetworkInterface