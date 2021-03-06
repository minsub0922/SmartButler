package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_counsel_select.*
import kotlinx.android.synthetic.main.activity_counsel_video_confirm.*

class CounselVideoConfirmActivity(
        override val layoutRes: Int=R.layout.activity_counsel_video_confirm,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()
        counselConfirm.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.counselConfirm -> {
                val i = Intent(this, CounselRecommendActivity::class.java)
                i.putExtra("type", "video")
                startActivity(i)
            }
        }
    }
}
