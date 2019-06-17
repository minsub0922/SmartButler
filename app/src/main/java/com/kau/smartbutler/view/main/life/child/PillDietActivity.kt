package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pill.*
import kotlinx.android.synthetic.main.activity_pill_diet.*

class PillDietActivity(
        override val layoutRes: Int= R.layout.activity_pill_diet,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()
        pillDietApply.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.pillDietApply-> {
                startActivity(Intent(this, PillDietApplyActivity::class.java))
            }
        }
    }
}
