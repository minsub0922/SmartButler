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
import com.kau.smartbutler.model.Diet
import com.kau.smartbutler.model.Meal
import com.kau.smartbutler.model.PersonalInformation
import com.kau.smartbutler.model.ProgressItem
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
    private val progressItemList by lazy { ArrayList<ProgressItem>() }
    var age: Int? = null
    var sex: String? = null
    var weight: Int? = null
    var goalWeight: Int? = null
    var activity:String? = null
    var requiredCal: Float = 0.toFloat()

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
            requiredCalorieSeekBar.text = test.requiredCalorie.toString()
            requiredCalorie.text = test.requiredCalorie.toString() + " kcal"
            requiredCal = test.requiredCalorie.toString().toFloat()
        }


        calorieSeekBar.thumb.mutate().alpha = 0
        initDataToSeekbar(0.toFloat(), 0.toFloat(), 0.toFloat(), requiredCal)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_morning -> {
                val i = Intent(this, DietCameraActivity::class.java)
                i.putExtra("date", intent.getLongExtra("date", 0))
                i.putExtra("meal", "breakfast")
                startActivityForResult(i, 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            Log.d("tag result ", data!!.getStringExtra("file") + "_" + data.getStringExtra("foodName"))
            realm.beginTransaction()
            var dietItem = realm.where<Diet>()
                    .equalTo("year", intent.getIntExtra("year", 0))
                    .equalTo("month", intent.getIntExtra("month", 0))
                    .equalTo("date", intent.getIntExtra("date", 0))
                    .findFirst()
            if (dietItem == null) {
                dietItem =  Diet(intent.getIntExtra("year", 0),
                        intent.getIntExtra("month", 0),
                        intent.getIntExtra("date", 0),
                        intent.getStringExtra("carbohydrate").toFloat(),
                        intent.getStringExtra("protein").toFloat(),
                        intent.getStringExtra("fat").toFloat())
                realm.copyToRealm(dietItem)
            } else {
                dietItem.carbo += intent.getStringExtra("carbohydrate").toFloat()
                dietItem.protein += intent.getStringExtra("protein").toFloat()
                dietItem.fat += intent.getStringExtra("fat").toFloat()
            }
            val totalCalorie = dietItem.carbo + dietItem.protein + dietItem.fat
            initDataToSeekbar(dietItem.carbo, dietItem.protein, dietItem.fat, requiredCal)
            tv_calorie.text = Math.round(totalCalorie).toString() + " kcal"
            realm.commitTransaction()
        } else {
            Log.d("tag result ", "refused")
        }

    }

    fun initDataToSeekbar(carbo: Float, protein: Float, fat: Float, required: Float) {
        var progressItem = ProgressItem(R.color.carbo, carbo / required)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.protein, protein / required)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.fat, fat / required)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.remainCalorie, 1.toFloat())
        progressItemList.add(progressItem)

        calorieSeekBar.initData(progressItemList)
        calorieSeekBar.invalidate()
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
