package com.kau.smartbutler.util.network

import android.telecom.Call
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*
import java.util.*

interface NetworkRouters {

    @POST("/rest/items/{deviceId}")
    fun postOrder(
            @Path("deviceId") deviceId: String,
            @Body order: String
    ): Observable<String>

    //이런식으로 라우터를 interface로 빼서 갖다 쓰면댐


//    @PUT("/api/busking/schedule/{schedule_id}")
//    fun putBuskingSchedules(
//            @Header("Authorization") Authorization: String,
//            @Body putBuskingScheduleRequest : putBuskingScheduleRequest): Single<IdResponse>

//    @GET("/api/busking/zones")
//    fun getBuskingSchedules(): Single<JsonObject>



////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //activity에서는 아래 처럼 갖다 쓰면 된다

    //post 예시

//    getNetworkInstance().postImage(
//    getSpStringData(tokenAtCookie),
//    filename = fileName,
//    image = image
//    )
//    .subscribeOn(Schedulers.io())
//    .observeOn(AndroidSchedulers.mainThread())
//    .subscribe({ urlResponse ->
//        if (urlResponse.okay) {
//            Log.d("tagg posterImage Url : ", urlResponse.URL)
//        }
//    }, { throwable ->
//        Log.d("tagg posterImageUploading fail : ", throwable.toString())
//        Toast.makeText(this, "네트워크 오류입니다. 관리자에게 문의하세요.", Toast.LENGTH_SHORT).show()
//    })



    //get 예시




//    getNetworkInstance().getBuskingSchedules().subscribeOn(Schedulers.io())
//    .observeOn(AndroidSchedulers.mainThread())
//    .subscribe({ urlResponse ->
//
//        urlResponse.remove("okay")
//
//        Log.d("tagg buskingZone : ",urlResponse.toString())
//
//        for (regionName in urlResponse.keySet()){
//
//            val buskingZones = ArrayList<BuskingZoneDetailInfo>()
//
//            for (obj in urlResponse.getAsJsonArray(regionName)) buskingZones.add(Gson().fromJson(obj, BuskingZoneDetailInfo::class.java)  )
//
//            buskingZoneList.add(BuskingZonesResponse(regionName, buskingZones))
//        }
//
//        for (region in buskingZoneList) {
//
//            //add title
//            modelList.add(BuskingZoneModel(name = region.regionName, type = 0))
//
//            //add zones
//            for (zones in region.buskingZoneDetailInfos) modelList.add(BuskingZoneModel(name = zones.name, type = 1))
//
//        }
//
//        adapter.notifyDataSetChanged()
//
//    }) { throwable ->
//        println(throwable.toString())
//    }



}