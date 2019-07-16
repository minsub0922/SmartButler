package com.kau.smartbutler.view.main.home.child.cctv

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cctv_set_domain.*

class CCTVSetDomainActivity(
        override val layoutRes : Int = R.layout.activity_cctv_set_domain,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        stateButton1.setOnClickListener {

        }
    }
}