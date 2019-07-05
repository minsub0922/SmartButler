package com.kau.smartbutler.view.main.butler

import android.content.Intent
import android.view.View
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_butler.*

class ButlerFragment : BaseFragment(), View.OnClickListener {

    override val layoutRes: Int = R.layout.fragment_butler


    companion object {
        var INSTANCE: ButlerFragment? = null

        fun getInstance(): ButlerFragment {
            if (INSTANCE == null)
                INSTANCE = ButlerFragment()
            return INSTANCE!!
        }

        fun newInstance(): ButlerFragment {
            return ButlerFragment()
        }
    }

    override fun setupView(view: View) {
        super.setupView(view)

        iv_butler_toggle.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.iv_butler_send -> {
                layout_butler_mic.visibility = View.GONE
                iv_butler_toggle.visibility = View.GONE
                layout_butler_text.visibility = View.VISIBLE
                iv_butler_send.visibility = View.VISIBLE


            }
        }
    }


}