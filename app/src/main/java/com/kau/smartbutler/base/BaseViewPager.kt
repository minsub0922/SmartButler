package com.kau.smartbutler.base
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

abstract class BaseViewPager {
    class Adapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        private val fragmentList = mutableListOf<BaseFragment>()
        private val fragmentTitles = mutableListOf<String>()

        fun addFragmentList(list: Array<BaseFragment>) {
            fragmentList.addAll(list)
        }

        fun addTitleList(array: Array<String>) {
            fragmentTitles.addAll(array)
        }

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getPageTitle(position: Int): CharSequence {
            return fragmentTitles[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun saveState(): Parcelable? {
            return null
        }
    }
}