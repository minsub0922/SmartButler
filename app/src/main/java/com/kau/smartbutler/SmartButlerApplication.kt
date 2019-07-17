package com.kau.smartbutler

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kau.smartbutler.util.network.listNetworkInit
import android.content.Intent
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import com.kau.smartbutler.util.network.networkInit
import io.realm.Realm
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class SmartButlerApplication: Application(){
    override fun onCreate() {
        super.onCreate()
        Thread(Runnable {
            run {
                // TODO Auto-generated method stub
                networkInit()
                listNetworkInit()
                Realm.init(this)
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