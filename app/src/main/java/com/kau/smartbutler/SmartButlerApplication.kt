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
    }
}