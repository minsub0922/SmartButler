package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import android.util.Log
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.util.network.getListNetworkInstance
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_device_light.*
import kotlinx.android.synthetic.main.activity_my_profile.*
import kotlinx.android.synthetic.main.activity_my_profile_modify.*

class MyPageModifyActivity(override val layoutRes: Int = R.layout.activity_my_profile_modify, override val isUseDatabinding: Boolean = false) : BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        myProfileModifyButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.myProfileModifyButton -> {
                getListNetworkInstance()
                        .getDailyCalorieRequirements(70, 29, "male", "good")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d("tagg result ", it.toString())
                        }
            }
        }
    }
}