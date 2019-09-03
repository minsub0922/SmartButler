package com.kau.smartbutler.util.network

import android.telecom.Call
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kau.smartbutler.model.PostCctvIpRequest
import com.kau.smartbutler.model.PostDetectionAreaRequest
import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*
import java.io.File
import java.util.*

interface NetworkRouters {

    @POST("/rest/items/{deviceId}")
    fun postOrder(
            @Path("deviceId") deviceId: String,
            @Body order: String
    ): Observable<String>

    @GET("/rest/items/{deviceId}")
    fun getDeviceInfos(
            @Path("deviceId") deviceId: String
    ): Observable<JsonObject>

    @GET("/rest/things")
    fun getDeviceNames(): Single<JsonArray>

    @GET("/event")
    fun getDetectedEvent(): Single<JsonObject>

    @POST("/cctv")
    fun postCctvIp(
            @Body cctv_ip: PostCctvIpRequest
    ): Single<JsonObject>

    @POST("/area")
    fun postDetectionArea(
            @Body area: PostDetectionAreaRequest
    ): Single<JsonObject>

    @GET("/dailyCalorieRequirements")
    fun getDailyCalorieRequirements(
            @Query("weight") weight: Int,
            @Query("age") age: Int,
            @Query("sex") sex: String,
            @Query("activity") activity: String
    ) : Observable<JsonObject>

    @Multipart
    @POST("/predict")
    fun predict(
        @Part image: MultipartBody.Part,
        @Part("file") filename: RequestBody
    ) : Observable<JsonArray>

    @GET("/nutrients")
    fun nutrients(
            @Query("label") label: String
    ) : Observable<JsonObject>

    @GET("/ids-utas/GetUserServlet")
    fun getUserServlet(
            @Query("mode") mode: String,
            @Query("stringInput") stringInput : String
    ) : Observable<String>
    //이런식으로 라우터를 interface로 빼서 갖다 쓰면댐
}