package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_cctv_detail.*

class CCTVDetailActivity(
        override val layoutRes : Int = R.layout.activity_cctv_detail,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {

    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        moreNavButton.setOnClickListener {
            startActivity(Intent(this, CCTVSetDomainActivity::class.java))
        }

    }

}