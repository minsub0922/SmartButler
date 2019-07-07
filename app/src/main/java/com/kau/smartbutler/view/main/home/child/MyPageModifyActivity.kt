package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import android.util.Log
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.util.network.getListNetworkInstance
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import kotlinx.android.synthetic.main.activity_my_profile_modify.*

class MyPageModifyActivity(override val layoutRes: Int = R.layout.activity_my_profile_modify, override val isUseDatabinding: Boolean = false) : BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    lateinit var realm: Realm
    var age: Int? = null
    override fun setupView() {
        super.setupView()

        myProfileModify.setOnClickListener(this)
        realm = Realm.getDefaultInstance()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.myProfileModify -> {
                age = et_age.text.toString().toInt()

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

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}