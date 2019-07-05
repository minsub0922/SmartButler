package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


var LIST_API_BASE_URL = "http://192.168.1.48:3003"

lateinit var listClient: OkHttpClient

lateinit var listRetrofit: Retrofit

val listNetworkInterface: NetworkRouters by lazy {  listRetrofit.create(NetworkRouters::class.java) }

fun listNetworkInit() {
    var logging = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    listClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(DecodeInterceptor())
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

    listRetrofit = Retrofit.Builder()
            .baseUrl(LIST_API_BASE_URL)
            .client(listClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            //.addConverterFactory(ScalarsConverterFactory.create())  //for string body
            .build()

}

fun getListNetworkInstance() = listNetworkInterface