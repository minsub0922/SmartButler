package com.kau.smartbutler.view.main.life.child

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity

class DietManagementActivity(
        override val layoutRes: Int= R.layout.activity_diet_management,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity() {
    override var isChildActivity: Boolean = true
}
