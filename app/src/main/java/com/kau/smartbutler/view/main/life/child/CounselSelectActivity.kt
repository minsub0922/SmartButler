package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_counsel_select.*

class CounselSelectActivity(
        override val layoutRes: Int=R.layout.activity_counsel_select,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()

        diet1.setOnClickListener(this)
        diet2.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diet1 -> {
                val i = Intent(this, CounselVideoConfirmActivity::class.java)
                i.putExtra("type", "video")
                startActivity(i)
            }
            R.id.diet2-> {
                val i = Intent(this, CounselRecommendActivity::class.java)
                i.putExtra("type", "voice")
                startActivity(i)
            }
        }
    }

}

