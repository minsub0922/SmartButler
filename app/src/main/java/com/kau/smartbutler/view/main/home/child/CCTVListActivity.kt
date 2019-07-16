package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.controller.CCTVListAdapter
import com.kau.smartbutler.model.CCTV
import kotlinx.android.synthetic.main.activity_cctv_list.*

class CCTVListActivity(override val isUseDatabinding: Boolean = false) : BaseActivity() {
    override val layoutRes: Int = R.layout.activity_cctv_list
    override var isChildActivity: Boolean = true
    val modelList = ArrayList<CCTV>()
    val adapter by lazy { CCTVListAdapter(this, modelList) }

    override fun setupView() {
        super.setupView()

        deviceListRecyclerView.adapter = adapter

        setDeviceList()

    }

    private fun setDeviceList(){

        modelList.add(CCTV(1,"현관"))
        modelList.add(CCTV(2,"침실"))
        adapter.notifyDataSetChanged()

    }



}