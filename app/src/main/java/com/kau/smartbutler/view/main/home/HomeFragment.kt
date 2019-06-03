package com.kau.smartbutler.view.main.home

import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.databinding.ObservableField
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import com.kau.smartbutler.controller.DeviceControllerAdpater
import com.kau.smartbutler.model.Device
import kotlinx.android.synthetic.main.fragment_home.*
import com.kau.smartbutler.util.recylcerview.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_home_state.*


class HomeFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragment_home
    val modelList = ArrayList<Device>()
    val adapter by lazy { DeviceControllerAdpater(activity!!, modelList, ObservableField(equals(false))) }


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
        rv_home_control.addItemDecoration(GridSpacingItemDecoration(3, 40, false))

        refrestOFF()

        btn_device_switch.setOnClickListener { v->

            for (model in modelList) model.state  = !(v as Switch).isChecked

            adapter.notifyDataSetChanged()

        }

    }


    private fun setModels(){

        modelList.add(Device(0,""))
        modelList.add(Device(1,"서재 조명"))
        modelList.add(Device(2,"안방 에어컨"))
        modelList.add(Device(3,"침실 조명"))
        modelList.add(Device(1,"안방 tv"))
        modelList.add(Device(2,"거실 tv"))
        modelList.add(Device(1,"거실 조명"))
        modelList.add(Device(1,"서재 조명"))
        modelList.add(Device(2,"안방 에어컨"))
        modelList.add(Device(3,"침실 조명"))
        modelList.add(Device(1,"안방 tv"))
        modelList.add(Device(2,"거실 tv"))
        modelList.add(Device(1,"거실 조명"))
    }
}