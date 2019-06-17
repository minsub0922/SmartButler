package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_counsel_recommend.*
import kotlinx.android.synthetic.main.activity_counsel_select.*
import kotlinx.android.synthetic.main.activity_counsel_video_confirm.*
import kotlin.math.log

class CounselRecommendActivity(
        override val layoutRes: Int=R.layout.activity_counsel_recommend,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()
        val intent = intent
        val type = intent.getStringExtra("type")
        Log.d("intent", type)
        if (type == "voice") {
            startCounsel.text = "전화상담 02-723-9988"
        } else {
            startCounsel.text = "상담 시작"
        }

        startCounsel.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.startCounsel -> {
                val intent = intent
                val type = intent.getStringExtra("type")
                if (type == "voice") {
                    val tel = "tel:027239988"
                    startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(tel)))
                } else {
                    startActivity(Intent(this, CounselVideoActivity::class.java))
                }

            }
        }
    }
}
