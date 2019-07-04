package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_diet.*
import kotlinx.android.synthetic.main.activity_diet_meal_confirm.*

class DietMealConfirmActivity(
        override val layoutRes: Int= R.layout.activity_diet_meal_confirm,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        btn_yes.setOnClickListener(this)
        btn_no.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_yes -> {
                val i = Intent(this, DietManagementActivity::class.java)
                startActivity(i)
            }
            R.id.btn_no-> {
                val i = Intent(this, DietCalendarActivity::class.java)
                startActivity(i)
            }
        }
    }
}