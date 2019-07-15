package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_counsel_select.*
import kotlinx.android.synthetic.main.activity_pill.*

class PillActivity(
        override val layoutRes: Int=R.layout.activity_pill,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()

        layout_pill_recognition.setOnClickListener(this)
        layout_my_pill.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.layout_pill_recognition -> {
                startActivity(Intent(this, PillIngredientActivity::class.java)) // 카메라에서 다음으로 넘어갈 수가 없어서 우선 성분 분석 뷰로 넘어감.
            }
            R.id.layout_my_pill-> {
                startActivity(Intent(this, PillManagementActivity::class.java))
            }
        }
    }
}