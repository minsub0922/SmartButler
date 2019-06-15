package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_my_profile.*

class MyPageActivity(override val layoutRes: Int = R.layout.activity_my_profile, override val isUseDatabinding: Boolean = false) : BaseActivity() {
    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        profileModifyButton.setOnClickListener{
            startActivity(Intent(this, MyPageModifyActivity::class.java))
        }
    }
}