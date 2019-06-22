package com.kau.smartbutler.view.main.nav

import android.util.Log
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.DeviceModeControllerAdpater
import com.kau.smartbutler.view.main.home.DeviceListSingleton
import com.kau.smartbutler.view.main.home.HomeFragment
import kotlinx.android.synthetic.main.activity_set_device_mode.*

class NavSetDeviceModeActivity(
        override val layoutRes: Int = R.layout.activity_set_device_mode,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {

    override var isChildActivity: Boolean = true
    val adapter by lazy { DeviceModeControllerAdpater(this, DeviceListSingleton.getInstance().list) }

    override fun setupView() {
        super.setupView()

        setDeviceModeRecyclerView.adapter = adapter

        btn_device_switch.setOnClickListener {

            adapter.switchState = !adapter.switchState

            adapter.notifyDataSetChanged()

        }

    }

    override fun onPause() {
        super.onPause()
        Log.d("tagg","onPause!!")
        HomeFragment.getInstance().updateDeviceList()
    }

}