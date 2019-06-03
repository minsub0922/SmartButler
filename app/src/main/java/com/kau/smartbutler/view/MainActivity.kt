package com.kau.smartbutler.view

import android.content.Intent
import android.os.Bundle

import com.google.android.material.navigation.NavigationView

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.util.fragment.BottomNavigationViewHelper
import com.kau.smartbutler.util.fragment.FragNavController
import com.kau.smartbutler.util.fragment.FragmentHistory
import com.kau.smartbutler.view.main.butler.ButlerFragment
import com.kau.smartbutler.view.main.home.HomeFragment
import com.kau.smartbutler.view.main.life.LifeFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main_content.*

class MainActivity(
        override val layoutRes: Int = R.layout.activity_main,
        override val isUseDatabinding: Boolean = false) :
        BaseActivity(),
        NavigationView.OnNavigationItemSelectedListener, FragNavController.TransactionListener, FragNavController.RootFragmentListener {

    private var preitem: Int = 0
    lateinit var TABS: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setDrawaerWithNavigationView()

        buildView(savedInstanceState)

        setToolbar()

    }

    private fun setDrawaerWithNavigationView(){


        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

    }

    private fun setToolbar(){

        supportActionBar!!.setDisplayShowTitleEnabled(false);

        supportActionBar!!

    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {

            if (mBackListener != null) mBackListener?.onBack()
            else if (mNavController?.isRootFragment == false) {
                mNavController?.popFragment()

            } else {

                if (fragmentHistory?.isEmpty == true) {
                    super.onBackPressed()

                } else {
                    if (fragmentHistory!!.stackSize > 1) {
                        val position = fragmentHistory!!.popPrevious()
                        switchTab(position)
                        navigation.getMenu().getItem(position).isChecked = true
                        preitem = position
                    } else {
                        navigation.getMenu().getItem(0).isChecked = true
                        switchTab(0)
                        preitem = 0
                        fragmentHistory!!.emptyStack()
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean { menuInflater.inflate(R.menu.main, menu); return true }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        return if (id == R.id.action_settings) { true } else super.onOptionsItemSelected(item)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        var pos = 0

        when (item.itemId) {
            R.id.nav_home -> pos = 0
            R.id.nav_butler -> pos = 1
            R.id.nav_life -> pos = 2
        }

        switchTab(pos)

        fragmentHistory!!.push(pos)

        preitem = pos

        true

    }

    private var mNavController: FragNavController? = null
    private var fragmentHistory: FragmentHistory? = null

    private fun buildView(savedInstanceState: Bundle?) {

        TABS = arrayOf(getString(R.string.main_menu_home_title), getString(R.string.main_menu_butler_title), getString(R.string.main_menu_life_title))

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        //navigation.setItemIconSize(resources.getDimension(R.dimen.main_menu_size).toInt())
        navigation.itemIconTintList = null

        BottomNavigationViewHelper.removeShiftMode(navigation)

        fragmentHistory = FragmentHistory()
        mNavController = FragNavController.newBuilder(savedInstanceState, getSupportFragmentManager(), R.id.main_activity_fragment_container)
                .transactionListener(this)
                .rootFragmentListener(this, TABS.size)
                .build()

        switchTab(0)


    }


    private fun switchTab(position: Int) = mNavController!!.switchTab(position)


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        mNavController?.onSaveInstanceState(outState)

    }

    override fun getRootFragment(index: Int): Fragment {

        when (index) {

            FragNavController.TAB1 -> return HomeFragment()
            FragNavController.TAB2 -> return ButlerFragment()
            FragNavController.TAB3 -> return LifeFragment()

        }
        throw IllegalStateException("Need to send an index that we know")
    }

    override fun onTabTransaction(fragment: Fragment?, index: Int) {

    }

    override fun onFragmentTransaction(fragment: Fragment?, transactionType: FragNavController.TransactionType) {
    }


    interface OnBackPressedListener {
        fun onBack()
    }

    private var mBackListener: OnBackPressedListener? = null

    fun setOnBackPressedListener(listener: OnBackPressedListener) {
        mBackListener = listener
    }

    //activity의 onActivityResult를 끌어쓰기.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //super.onActivityResult(requestCode, resultCode, data);
        val fragment = supportFragmentManager.findFragmentById(R.id.main_activity_fragment_container)
        fragment!!.onActivityResult(requestCode, resultCode, data)

    }

}
