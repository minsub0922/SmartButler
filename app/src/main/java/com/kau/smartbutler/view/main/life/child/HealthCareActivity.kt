package com.kau.smartbutler.view.main.life.child

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity

class HealthCareActivity(
        override val layoutRes: Int= R.layout.activity_health_care,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity() {
    override var isChildActivity: Boolean = true
}
