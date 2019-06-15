package com.kau.smartbutler.view.main.home.child

import android.util.Log
import android.view.View
import android.widget.Button
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_device_aircon.*

class DeviceAirconActivity(
        override val layoutRes: Int = R.layout.activity_device_aircon,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity(), View.OnClickListener{

    override var isChildActivity: Boolean = true

    val stateButtonArray by lazy {
        arrayOf(stateButton1, stateButton2, stateButton3, stateButton4)
    }

    override fun setupView() {
        super.setupView()

        powerButton.isSelected = true

        for (b in stateButtonArray) b.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        for (b in stateButtonArray) b.isSelected = b.id == v!!.id
    }


}