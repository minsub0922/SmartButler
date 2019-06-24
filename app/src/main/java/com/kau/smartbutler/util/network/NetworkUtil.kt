package com.kau.smartbutler.util.network


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


var API_BASE_URL = "http:// sample url "

lateinit var client: OkHttpClient

lateinit var retrofit: Retrofit

lateinit var networkInterface: NetworkRouters

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
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .build()

    networkInterface = retrofit.create(NetworkRouters::class.java)
}

fun getNetworkInstance() = networkInterface