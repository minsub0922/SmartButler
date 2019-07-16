package com.kau.smartbutler

import android.app.Application
import android.content.Intent
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import com.kau.smartbutler.util.network.networkInit
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SmartButlerApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        Thread(Runnable {
            run {
                // TODO Auto-generated method stub
                networkInit()
            }
        }).start()

        getEvent()

    }

    fun getEvent(){


        //이벤트 감지 및 페이지 접근

//        getCCTVNetworkInstance()
//                .getDetectedEvent()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe({ res ->
//                  //startActivity(Intent(this, className ))
//                })



    }
}