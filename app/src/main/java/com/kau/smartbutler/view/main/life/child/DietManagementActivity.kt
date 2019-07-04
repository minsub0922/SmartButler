package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.google.gson.Gson
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.util.network.getNetworkInstance
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_diet_management.*
import kotlinx.android.synthetic.main.activity_diet_order.*

class DietManagementActivity(
        override val layoutRes: Int= R.layout.activity_diet_management,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity(), View.OnClickListener {
    override var isChildActivity: Boolean = true

    override fun setupView() {
        super.setupView()

        iv_morning.setOnClickListener(this)

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
}
