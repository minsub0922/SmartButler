package com.kau.smartbutler.util.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class DecodeInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var stringurl = request.url().toString()

        return if (stringurl.contains("/api/clip/list")) {
            Log.d("Decoded ", "contains /api/clip/list")
            stringurl = stringurl.replace("%3F", "?")

            var newRequest = Request.Builder()
                    .headers(request.headers())
                    .url(stringurl)
                    .build()

            chain.proceed(newRequest)
        } else {
            Log.d("Decoded ", "default")
            chain.proceed(request)
        }
    }
}