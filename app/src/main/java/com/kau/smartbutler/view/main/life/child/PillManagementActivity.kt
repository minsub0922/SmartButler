package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.PilManagementListAdapter
import com.kau.smartbutler.controller.PillCautionListAdapter
import com.kau.smartbutler.model.Pill
import com.kau.smartbutler.model.PillCaution
import kotlinx.android.synthetic.main.activity_pill_caution.*
import kotlinx.android.synthetic.main.activity_pill_management.*

class PillManagementActivity(
        override val layoutRes: Int=R.layout.activity_pill_management,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(){
    override var isChildActivity: Boolean = true
    val pillList = ArrayList<Pill>()
    val pilldapter by lazy { PilManagementListAdapter(this, pillList) }

    override fun setupView() {
        super.setupView()
        pillManagementRecyclerView.adapter = pilldapter
        setDeviceList()

    }

    private fun setDeviceList(){

        pillList.add(Pill("혈압약", "ZS프로펜, 프로비타민", "스타틴과 함께 복용 금지", "어지러움이나 두통을 호소할 수 있음.", "고혈압", "토마토, 양파, 가지", "고혈압"))

        pilldapter.notifyDataSetChanged()


    }

}