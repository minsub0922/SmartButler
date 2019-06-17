package com.kau.smartbutler.view.main.life.child

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.PillCautionListAdapter
import com.kau.smartbutler.model.PillCaution
import kotlinx.android.synthetic.main.activity_pill_caution.*

class PillCautionActivity(
        override val layoutRes: Int= R.layout.activity_pill_caution,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity() {
    override var isChildActivity: Boolean = true
    val pillCautionList = ArrayList<PillCaution>()
    val pillCautionAdapter by lazy { PillCautionListAdapter(this, pillCautionList) }

    override fun setupView() {
        super.setupView()
        pillCautionRecyclerView.adapter = pillCautionAdapter

        setDeviceList()

    }

    private fun setDeviceList(){

        pillCautionList.add(PillCaution("혈압약"))
        pillCautionList.add(PillCaution("당뇨약"))

        pillCautionAdapter.notifyDataSetChanged()


    }
}