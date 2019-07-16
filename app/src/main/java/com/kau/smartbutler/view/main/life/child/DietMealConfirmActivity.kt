package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_diet.*
import kotlinx.android.synthetic.main.activity_diet_meal_confirm.*
import java.io.File
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import android.graphics.Matrix
import android.net.Uri
import android.os.Handler
import android.util.Log
import com.google.gson.GsonBuilder
import com.kau.smartbutler.util.network.getListNetworkInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.nio.file.Files.exists



class DietMealConfirmActivity(
        override val layoutRes: Int= R.layout.activity_diet_meal_confirm,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        btn_yes.setOnClickListener(this)
        btn_no.setOnClickListener(this)
        Log.d("tag result ", "intent : " + intent.getStringExtra("file"))

        val imgFile = File(intent.getStringExtra("file"))
        val uri = Uri.fromFile(imgFile.absoluteFile)
        val imageStream = contentResolver.openInputStream(uri)
        val selectedImage = BitmapFactory.decodeStream(imageStream)
        Log.d("tag result ", "intent : " + uri.toString())
        val matrix = Matrix();
        matrix.postRotate(90.toFloat());
        val scaledBitmap = Bitmap.createScaledBitmap(selectedImage, selectedImage.width, selectedImage.height, true);
        val rotatedBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.width, scaledBitmap.height, matrix, true);
        iv_food.setImageBitmap(rotatedBitmap)

        var foodName = intent.getStringExtra("foodName").toString()
        foodName = foodName.substring(1, foodName.length - 1)
        et_food_name.setText(foodName)
        refreshButton.setOnClickListener(this)

        getListNetworkInstance()
                .nutrients(foodName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("tag result ", it.toString())
                    val gson = GsonBuilder().setPrettyPrinting().create()
                    val nutrients : Array<Float> = gson.fromJson(it.get("nutrients"), Array<Float>::class.java)
                    carbohydrate_calorie.text = nutrients[0].toString()
                    protein_calorie.text = nutrients[1].toString()
                    fat_calorie.text = nutrients[2].toString()
                }

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_yes -> {
                val i = intent
                i.putExtra("carbohydrate", carbohydrate_calorie.text.toString())
                i.putExtra("protein", protein_calorie.text.toString())
                i.putExtra("fat", fat_calorie.text.toString())
                setResult(200, intent)
                finish()
            }
            R.id.btn_no-> {
                val i = intent
                setResult(500, i)
                finish()
            }
            R.id.refreshButton -> {
                val foodName = et_food_name.text.toString()
                getListNetworkInstance()
                        .nutrients(foodName)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            Log.d("tag result ", it.toString())
                            val gson = GsonBuilder().setPrettyPrinting().create()
                            val nutrients : Array<Float> = gson.fromJson(it.get("nutrients"), Array<Float>::class.java)
                            carbohydrate_calorie.text = nutrients[0].toString()
                            protein_calorie.text = nutrients[1].toString()
                            fat_calorie.text = nutrients[2].toString()

                        }
            }
            R.id.refreshIntake -> {
                val intake = et_intake.text.toString().toFloat()
                carbohydrate_calorie.text = (carbohydrate_calorie.text.toString().toFloat() * intake).toString()
                protein_calorie.text = (protein_calorie.text.toString().toFloat() * intake).toString()
                fat_calorie.text = (fat_calorie.text.toString().toFloat() * intake).toString()
            }
        }
    }
}