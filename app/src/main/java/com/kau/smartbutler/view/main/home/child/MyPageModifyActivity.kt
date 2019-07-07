package com.kau.smartbutler.view.main.home.child

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.util.network.getListNetworkInstance
import io.reactivex.schedulers.Schedulers
import io.reactivex.android.schedulers.AndroidSchedulers
import io.realm.Realm
import io.realm.kotlin.createObject
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_my_profile_modify.*

class MyPageModifyActivity(override val layoutRes: Int = R.layout.activity_my_profile_modify, override val isUseDatabinding: Boolean = false) : BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    var realm = Realm.getDefaultInstance()
    var info: PersonalInformation? = null

    var age: Int? = null
    var sex: String? = null
    var weight: Int? = null
    var goalWeight: Int? = null
    var activity:String? = null

    override fun setupView() {
        super.setupView()

        myProfileModify.setOnClickListener(this)
        realm.beginTransaction()
        info = realm.where<PersonalInformation>().findFirst()
        realm.commitTransaction()
        /*if (info != null) {
            spinnerSex.setSelection(getSexPosition(info!!.sex))
            spinnerActivity.setSelection(getActivityPosition(info!!.activity))
            et_age.setText(info!!.age)
            et_weight.setText(info!!.weight)
            et_goal_weight.setText(info!!.goalWeight)
        }*/

        val adapterSex = ArrayAdapter.createFromResource(this, R.array.sex, android.R.layout.simple_spinner_item)
        spinnerSex.adapter = adapterSex
        spinnerSex.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sex = spinnerSex.getItemAtPosition(p2).toString()
            }
        }

        val adapterActivity = ArrayAdapter.createFromResource(this, R.array.activity, android.R.layout.simple_spinner_item)
        spinnerActivity.adapter = adapterActivity
        spinnerActivity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                activity = spinnerActivity.getItemAtPosition(p2).toString()
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.myProfileModify -> {
                if (info == null) {
                    age = if(et_age.text.toString() == "") 30 else et_age.text.toString().toInt()
                    weight = if(et_weight.text.toString() == "") 70 else et_weight.text.toString().toInt()
                    goalWeight = if(et_goal_weight.text.toString() == "") 70 else et_goal_weight.text.toString().toInt()
                    realm.beginTransaction()
                    info = PersonalInformation(age!!, sex!!, weight!!, goalWeight!!, activity!!)
                    realm.copyToRealm(info!!)
                    realm.commitTransaction()

                    Log.d("tag result ", age.toString())
                    realm.executeTransaction {
                        val test = realm.where(PersonalInformation::class.java).findFirstAsync()
                        Log.d("tag result ", test.age.toString())
                    }
                } else {
                    realm.beginTransaction()
                    val modifyInfo = realm.where(PersonalInformation::class.java).findFirstAsync()
                    modifyInfo.age = if(et_age.text.toString() == "") 30 else et_age.text.toString().toInt()
                    modifyInfo.weight = if(et_weight.text.toString() == "") 70 else et_weight.text.toString().toInt()
                    modifyInfo.goalWeight = if(et_goal_weight.text.toString() == "") 70 else et_goal_weight.text.toString().toInt()
                    modifyInfo.sex = sex!!
                    modifyInfo.activity = activity!!
                    realm.commitTransaction()

                    realm.executeTransaction {
                        val test = realm.where(PersonalInformation::class.java).findFirstAsync()
                        Log.d("tag result modify ", test.age.toString())
                    }
                }



                /*getListNetworkInstance()
                        .getDailyCalorieRequirements(70, 29, "male", "good")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d("tag result ", it.toString())
                        }*/
            }
        }
    }
    fun getSexPosition(sex: String): Int {
        when(sex) {
            "남" -> { return 1}
            "여" -> { return 2}
        }
        return -1
    }

    fun getActivityPosition(activity: String): Int {
        when(activity) {
            "하" -> { return 1}
            "중" -> { return 2}
            "상" -> { return 3}
        }
        return -1
    }
    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}