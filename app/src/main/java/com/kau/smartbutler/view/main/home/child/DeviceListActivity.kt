package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.DeviceListAdapter
import com.kau.smartbutler.model.Device
import kotlinx.android.synthetic.main.activity_device_list.*

class DeviceListActivity(override val isUseDatabinding: Boolean = false) : BaseActivity() {
    override val layoutRes: Int = R.layout.activity_device_list
    override var isChildActivity: Boolean = true
    val modelList = ArrayList<Device>()
    val adapter by lazy { DeviceListAdapter(this, modelList) }

    override fun setupView() {
        super.setupView()

        deviceListRecyclerView.adapter = adapter

        setDeviceList()

        deviceAddButton.setOnClickListener{ startActivity(Intent(this, DeviceAddActivity::class.java)) }

    }

    private fun setDeviceList(){

        modelList.add(Device(1,"서재 조명"))
        modelList.add(Device(2,"서재 조명"))
        modelList.add(Device(3,"서재 조명"))
        modelList.add(Device(4,"서재 조명"))
        adapter.notifyDataSetChanged()

    }



}