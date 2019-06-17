package com.kau.smartbutler.view.main.life

import android.content.Intent
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import com.kau.smartbutler.view.main.life.child.*
import kotlinx.android.synthetic.main.fragment_life.*

class LifeFragment : BaseFragment(), View.OnClickListener {

    override val layoutRes: Int = R.layout.fragment_life

    companion object {
        var INSTANCE: LifeFragment? = null

        fun getInstance(): LifeFragment {
            if (INSTANCE == null)
                INSTANCE = LifeFragment()
            return INSTANCE!!
        }

        fun newInstance(): LifeFragment {
            return LifeFragment()
        }
    }

    override fun setupView(view: View) {
        super.setupView(view)

        btn_life_counsel.setOnClickListener(this)
        btn_life_food.setOnClickListener(this)
        btn_life_pill.setOnClickListener(this)
        btn_life_res.setOnClickListener(this)
        btn_life_vr.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){

            R.id.btn_life_counsel -> startActivity(Intent(activity, CounselSelectActivity::class.java))
            R.id.btn_life_pill -> startActivity(Intent(activity, PillActivity::class.java))
            R.id.btn_life_vr -> startActivity(Intent(activity, HealthCareActivity::class.java))
            R.id.btn_life_res -> startActivity(Intent(activity, ReservationActivity::class.java))
            R.id.btn_life_food -> startActivity(Intent(activity, DietActivity::class.java))

        }
    }


}