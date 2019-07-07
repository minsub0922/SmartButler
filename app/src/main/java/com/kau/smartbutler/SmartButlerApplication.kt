package com.kau.smartbutler

import android.app.Application
import com.kau.smartbutler.util.network.listNetworkInit
import com.kau.smartbutler.util.network.networkInit
import io.realm.Realm

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
    }
}