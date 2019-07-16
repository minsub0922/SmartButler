package com.kau.smartbutler

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.kau.smartbutler.util.network.listNetworkInit
import com.kau.smartbutler.util.network.networkInit
import io.realm.Realm

open class SmartButlerApplication: Application(){
    private val PERMISSIONS_REQUEST_CODE = 100
    private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO)
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