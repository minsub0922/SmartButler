package com.kau.smartbutler

import android.app.Application
import com.kau.smartbutler.util.network.networkInit

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