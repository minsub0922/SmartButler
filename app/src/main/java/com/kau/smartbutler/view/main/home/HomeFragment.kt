package com.kau.smartbutler.view.main.home

import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import com.kau.smartbutler.controller.DeviceControllerAdpater
import com.kau.smartbutler.model.Device
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragment_home
    val modelList = ArrayList<Device>()
    val adapter by lazy { DeviceControllerAdpater(activity!!, modelList) }


    companion object {
        var INSTANCE: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (INSTANCE == null)
                INSTANCE = HomeFragment()
            return INSTANCE!!
        }

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun setupView(view: View) {

        setModels()

        rv_home_control.adapter = adapter

    }

    private fun setModels(){

        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))
        modelList.add(Device(0,"asd"))

    }



}