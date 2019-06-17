package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_pill.*
import kotlinx.android.synthetic.main.activity_pill_ingredient.*

class PillIngredientActivity(
        override val layoutRes: Int=R.layout.activity_pill_ingredient,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true
    override fun setupView() {
        super.setupView()

        registerPill.setOnClickListener(this)
        contactHospital.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.contactHospital -> {
                val tel = "tel:027239988"
                startActivity(Intent(Intent.ACTION_DIAL, Uri.parse(tel)))
            }
            R.id.registerPill-> {
                startActivity(Intent(this, PillCautionActivity::class.java))
            }
        }
    }

}
