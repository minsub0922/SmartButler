package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


var API_BASE_URL = "http://192.168.1.48:8080"

lateinit var client: OkHttpClient

lateinit var retrofit: Retrofit

val networkInterface: NetworkRouters by lazy {  retrofit.create(NetworkRouters::class.java) }

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

}

fun getNetworkInstance() = networkInterface