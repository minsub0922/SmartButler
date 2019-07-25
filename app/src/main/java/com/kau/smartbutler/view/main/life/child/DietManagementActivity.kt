package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
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
import io.realm.RealmResults
import io.realm.kotlin.where
import kotlinx.android.synthetic.main.activity_diet_management.*
import kotlinx.android.synthetic.main.activity_diet_meal_confirm.*
import kotlinx.android.synthetic.main.activity_diet_order.*
import java.io.File

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
    var requiredCal: Float = 1880.toFloat()
    lateinit var personalInformation: PersonalInformation
    lateinit var mealItemList: RealmResults<Meal>
    var initCarbohydrate = 0.toFloat()
    var initProtein = 0.toFloat()
    var initFat = 0.toFloat()


    override fun setupView() {
        super.setupView()

        iv_morning.setOnClickListener(this)
        iv_lunch.setOnClickListener(this)
        iv_dinner.setOnClickListener(this)
        iv_diet_etc.setOnClickListener(this)


        realm.executeTransactionAsync (
                { bgRealm: Realm ->
                    personalInformation = bgRealm.where<PersonalInformation>().findFirst()!!
                    et_age.setText(personalInformation.age.toString())
                    et_weight.setText(personalInformation.weight.toString())
                    et_goal_weight.setText(personalInformation.goalWeight.toString())
                    spinner_sex.setText(personalInformation.sex.toString())
                    spinner_activity.setText(personalInformation.activity.toString())
                    requiredCalorieSeekBar.text = personalInformation.requiredCalorie.toString()
                    requiredCalorie.text = personalInformation.requiredCalorie.toString() + " kcal"
                    requiredCal = personalInformation.requiredCalorie.toString().toFloat()
                },
                {

                },
                {

                }
        )
        realm.executeTransactionAsync(
                { bgRealm: Realm ->
                    Log.d("result", intent.getLongExtra("year", 0).toString() + " " + intent.getLongExtra("month", 0).toString() + " " + intent.getLongExtra("date", 0).toString())
                    mealItemList = bgRealm.where<Meal>()
                            .equalTo("year", intent.getLongExtra("year", 0))
                            .equalTo("month", intent.getLongExtra("month", 0))
                            .equalTo("day", intent.getLongExtra("date", 0))
                            .findAll()
                    Log.d("result ", mealItemList.size.toString())
                    for(meal in mealItemList) {
                        setThumbnail(meal)
                        initCarbohydrate += meal.carbo
                        initProtein += meal.protein
                        initFat += meal.fat
                    }
                    initDataToSeekbar(initCarbohydrate, initProtein, initFat, requiredCal)
                },
                {

                },
                {
                    initDataToSeekbar(initCarbohydrate, initProtein, initFat, requiredCal)
                }
        )
        calorieSeekBar.thumb.mutate().alpha = 0

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_morning -> {
                val i = Intent(this, DietCameraActivity::class.java)
                i.putExtra("time", intent.getLongExtra("time", 0))
                i.putExtra("year", intent.getLongExtra("year", 0))
                i.putExtra("month", intent.getLongExtra("month", 0))
                i.putExtra("date", intent.getLongExtra("date", 0))
                i.putExtra("type", "breakfast")
                startActivityForResult(i, 200)
            }
            R.id.iv_lunch -> {
                val i = Intent(this, DietCameraActivity::class.java)
                i.putExtra("time", intent.getLongExtra("time", 0))
                i.putExtra("year", intent.getLongExtra("year", 0))
                i.putExtra("month", intent.getLongExtra("month", 0))
                i.putExtra("date", intent.getLongExtra("date", 0))
                i.putExtra("type", "lunch")
                startActivityForResult(i, 200)
            }
            R.id.iv_dinner -> {
                val i = Intent(this, DietCameraActivity::class.java)
                i.putExtra("time", intent.getLongExtra("time", 0))
                i.putExtra("year", intent.getLongExtra("year", 0))
                i.putExtra("month", intent.getLongExtra("month", 0))
                i.putExtra("date", intent.getLongExtra("date", 0))
                i.putExtra("type", "dinner")
                startActivityForResult(i, 200)
            }
            R.id.iv_diet_etc -> {
                val i = Intent(this, DietCameraActivity::class.java)
                i.putExtra("time", intent.getLongExtra("time", 0))
                i.putExtra("year", intent.getLongExtra("year", 0))
                i.putExtra("month", intent.getLongExtra("month", 0))
                i.putExtra("date", intent.getLongExtra("date", 0))
                i.putExtra("type", "etc")
                startActivityForResult(i, 200)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 200) {
            Log.d("tag result ", data!!.getStringExtra("file") + "_" + data.getStringExtra("foodName"))
            realm.executeTransactionAsync(
                    {
                        Log.d("result", data.getLongExtra("year", 0).toString() + " " + data.getLongExtra("month", 0).toString() + " " + data.getLongExtra("date", 0).toString())
                        mealItemList = it.where<Meal>()
                                .equalTo("year", data.getLongExtra("year", 0))
                                .equalTo("month", data.getLongExtra("month", 0))
                                .equalTo("day", data.getLongExtra("date", 0))
                                .findAll()
                        for(meal in mealItemList) {
                            setThumbnail(meal)
                            initCarbohydrate += meal.carbo
                            initProtein += meal.protein
                            initFat += meal.fat
                        }
                        initDataToSeekbar(initCarbohydrate, initProtein, initFat, requiredCal)
                        tv_calorie.text = Math.round(initCarbohydrate + initProtein + initFat).toString() + " kcal"
                    },
                    {

                    },
                    {
                        initDataToSeekbar(initCarbohydrate, initProtein, initFat, requiredCal)
                        tv_calorie.text = Math.round(initCarbohydrate + initProtein + initFat).toString() + " kcal"
                    }
            )

        } else {
            Log.d("tag result ", "refused")
        }

    }

    fun initDataToSeekbar(carbo: Float, protein: Float, fat: Float, required: Float) {
        Log.d("result ", carbo.toString() + " " + protein.toString() + " " + fat.toString() + " " + required.toString() + " ")
        progressItemList.clear()
        var progressItem = ProgressItem(R.color.carbo, carbo / required * 100)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.protein, protein / required * 100)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.fat, fat / required * 100)
        progressItemList.add(progressItem)

        progressItem = ProgressItem(R.color.remainCalorie, 1.toFloat())
        progressItemList.add(progressItem)

        calorieSeekBar.initData(progressItemList)
        calorieSeekBar.invalidate()
    }

    fun setThumbnail(type: String, year: Long, month: Long, date: Long) {
        val meal = realm.where<Meal>()
                .equalTo("year", year)
                .equalTo("month", month)
                .equalTo("day", date)
                .equalTo("type", type)
                .findFirst()
        val imgFile = File(meal!!.fileDir)
        val uri = Uri.fromFile(imgFile.absoluteFile)
        val imageStream = contentResolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val matrix = Matrix();
        matrix.postRotate(90.toFloat());
        val scaledBitmap = Bitmap.createScaledBitmap(selectedImage, iv_morning.width, iv_morning.height, true);
        val rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true);
        when(type) {
            "breakfast" -> {
                iv_morning.setImageBitmap(rotatedBitmap)
            }
            "lunch" -> {
                iv_lunch.setImageBitmap(rotatedBitmap)
            }
            "dinner" -> {
                iv_dinner.setImageBitmap(rotatedBitmap)
            }
            "etc" -> {
                iv_diet_etc.setImageBitmap(rotatedBitmap)
            }
        }
    }

    fun setThumbnail(meal: Meal) {
        val imgFile = File(meal.fileDir)
        val uri = Uri.fromFile(imgFile.absoluteFile)
        val imageStream = contentResolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        val matrix = Matrix();
        matrix.postRotate(90.toFloat());
        val scaledBitmap = Bitmap.createScaledBitmap(selectedImage, iv_morning.width, iv_morning.height, true);
        val rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true);
        when(meal.type) {
            "breakfast" -> {
                iv_morning.setImageBitmap(rotatedBitmap)
            }
            "lunch" -> {
                iv_lunch.setImageBitmap(rotatedBitmap)
            }
            "dinner" -> {
                iv_dinner.setImageBitmap(rotatedBitmap)
            }
            "etc" -> {
                iv_diet_etc.setImageBitmap(rotatedBitmap)
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
