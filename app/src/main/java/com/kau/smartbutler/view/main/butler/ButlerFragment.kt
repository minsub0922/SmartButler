package com.kau.smartbutler.view.main.butler

import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseFragment

class ButlerFragment : BaseFragment() {

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

}