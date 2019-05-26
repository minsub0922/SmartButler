package com.kau.smartbutler.view.main.life

import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment

class LifeFragment : BaseFragment() {

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

}