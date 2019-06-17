package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_counsel_select.*
import kotlinx.android.synthetic.main.activity_diet.*

class DietActivity(
        override val layoutRes: Int=R.layout.activity_diet,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()

        dietManagement.setOnClickListener(this)
        dietOrder.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.dietManagement -> {
                val i = Intent(this, DietCalendarActivity::class.java)
                i.putExtra("type", "manage")
                startActivity(i)
            }
            R.id.dietOrder-> {
                val i = Intent(this, DietCalendarActivity::class.java)
                i.putExtra("type", "order")
                startActivity(i)
            }
        }
    }
}

