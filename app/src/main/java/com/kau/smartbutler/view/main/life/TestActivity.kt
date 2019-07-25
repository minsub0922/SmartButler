package com.kau.smartbutler.view.main.life

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.view.main.life.child.CounselRecommendActivity
import kotlinx.android.synthetic.main.activity_counsel_video_confirm.*

class TestActivity (
        override val layoutRes: Int= R.layout.activity_test,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(){
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()
    }
}
