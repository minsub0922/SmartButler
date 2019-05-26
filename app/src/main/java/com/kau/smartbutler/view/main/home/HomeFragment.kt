package com.kau.smartbutler.view.main.home

import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment

class HomeFragment : BaseFragment() {

    override val layoutRes: Int = R.layout.fragment_home

    companion object {
        var INSTANCE: HomeFragment? = null

        fun getInstance(): HomeFragment {
            if (INSTANCE == null)
                INSTANCE = HomeFragment()
            return INSTANCE!!
        }

        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

}