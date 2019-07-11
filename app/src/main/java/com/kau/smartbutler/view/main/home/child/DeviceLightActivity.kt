package com.kau.smartbutler.view.main.home.child
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_device_light.*

class DeviceLightActivity(override val layoutRes: Int = R.layout.activity_device_light
                          , override val isUseDatabinding: Boolean = false) : BaseActivity() {

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        lightUpButton.setOnClickListener { v  ->
            lightSeekbar.progress = lightSeekbar.progress + 20
        }
        lightDownButton.setOnClickListener{v ->
            if (lightSeekbar.progress > 0) lightSeekbar.progress = lightSeekbar.progress - 20
        }
    }

}