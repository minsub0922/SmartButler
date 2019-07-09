package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.gson.Gson
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_diet_management.*
import kotlinx.android.synthetic.main.activity_diet_order.*

class DietManagementActivity(
        override val layoutRes: Int= R.layout.activity_diet_management,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    private var realm = Realm.getDefaultInstance()
    var age: Int? = null
    var sex: String? = null
    var weight: Int? = null
    var goalWeight: Int? = null
    var activity:String? = null

    override fun setupView() {
        super.setupView()

        iv_morning.setOnClickListener(this)


        val adapterSex = ArrayAdapter.createFromResource(this, R.array.sex, R.layout.my_profile_spinner_item)
        spinner_sex.adapter = adapterSex
        spinner_sex.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sex = spinner_sex.getItemAtPosition(p2).toString()
            }
        }

        val adapterActivity = ArrayAdapter.createFromResource(this, R.array.activity, R.layout.my_profile_spinner_item)
        spinner_activity.adapter = adapterActivity
        spinner_activity.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                activity = spinner_activity.getItemAtPosition(p2).toString()
            }
        }

        realm.beginTransaction()
        val test = realm.where<PersonalInformation>().findFirst()
        realm.commitTransaction()
        if(test != null) {
            et_age.setText(test.age.toString())
            et_weight.setText(test.weight.toString())
            et_goal_weight.setText(test.goalWeight.toString())
            spinner_sex.setSelection(getSexPosition(test.sex))
            spinner_activity.setSelection(getActivityPosition(test.activity))
            tv_need_calorie.text = test.requiredCalorie.toString() + " kcal"
        }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_morning -> {
                val i = Intent(this, DietCameraActivity::class.java)

                i.putExtra("meal", "morning")
                startActivity(i)
            }
        }
    }

    fun getSexPosition(sex: String): Int {
        when(sex) {
            "남" -> { return 0}
            "여" -> { return 1}
        }
        return -1
    }

    fun getActivityPosition(activity: String): Int {
        when(activity) {
            "상" -> { return 0}
            "중" -> { return 1}
            "하" -> { return 2}
        }
        return -1
    }
}
