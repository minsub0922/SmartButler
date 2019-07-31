package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import com.kau.smartbutler.model.Profile
import io.realm.Realm
import io.realm.kotlin.where
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


var API_BASE_URL = "http://112.169.29.116:8080"
var CCTV_ANALYSIS_URL = "http://172.16.16.87:10005"

lateinit var client: OkHttpClient

lateinit var retrofit: Retrofit
lateinit var retrofitForJson: Retrofit
lateinit var retrofitForCCTV: Retrofit

lateinit var networkInterface: NetworkRouters
lateinit var networkInterfaceForJsonTypes: NetworkRouters
lateinit var cctvNetworkInterface: NetworkRouters

fun networkInit() {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    val realm = Realm.getDefaultInstance()
    realm.beginTransaction()
    val initialProfile = realm.where<Profile>(Profile::class.java).findFirst()
    realm.commitTransaction()
    if (initialProfile != null) {
        API_BASE_URL = "http://${initialProfile.openhapIP}:8080"
        CCTV_ANALYSIS_URL = "http://${initialProfile.serverIP}:${initialProfile.serverPort}"
    }

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
            .baseUrl(CCTV_ANALYSIS_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    networkInterface = retrofit.create(NetworkRouters::class.java)
    networkInterfaceForJsonTypes = retrofitForJson.create(NetworkRouters::class.java)
    cctvNetworkInterface = retrofitForCCTV.create(NetworkRouters::class.java)
}

fun getNetworkInstance() = networkInterface

fun getNetworkInstanceForJson() = networkInterfaceForJsonTypes

fun getCCTVNetworkInstance() = cctvNetworkInterface