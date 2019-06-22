package com.kau.smartbutler.view.main.home

import com.kau.smartbutler.model.Device

class DeviceListSingleton() {

    val list by lazy {
        ArrayList<Device>()
    }

    companion object {
        var INSTANCE: DeviceListSingleton? = null

        fun getInstance(): DeviceListSingleton {
            if (INSTANCE == null)
                INSTANCE = DeviceListSingleton()
            return INSTANCE!!
        }

        fun newInstance(): DeviceListSingleton {
            return DeviceListSingleton()
        }
    }
}