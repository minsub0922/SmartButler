package com.kau.smartbutler.view.main.home.child

import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity

class DeviceAddActivity(
        override val layoutRes : Int = R.layout.activity_device_add,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

    }

}