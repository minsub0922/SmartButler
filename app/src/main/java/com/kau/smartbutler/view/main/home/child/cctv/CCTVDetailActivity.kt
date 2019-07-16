package com.kau.smartbutler.view.main.home.child.cctv

import android.content.Intent
import com.google.gson.JsonObject
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.PostDetectionAreaRequest
import com.kau.smartbutler.util.network.getCCTVNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_cctv_detail.*

class CCTVDetailActivity(
        override val layoutRes : Int = R.layout.activity_cctv_detail,
        override val isUseDatabinding: Boolean = false)
    : BaseActivity() {

    override var isChildActivity: Boolean = true

    lateinit var res : JsonObject

    override fun setupView() {
        super.setupView()

        moreNavButton.setOnClickListener {
            startActivity(Intent(this, CCTVSetDomainActivity::class.java))
        }


        //네트워크 Examples
        getCCTVNetworkInstance()
                .postDetectionArea(PostDetectionAreaRequest("","","",""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ res ->

                    this.res = res

                })
    }

}