package com.kau.smartbutler.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log

import com.google.android.material.navigation.NavigationView

import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import com.kau.smartbutler.util.fragment.BottomNavigationViewHelper
import com.kau.smartbutler.util.fragment.FragNavController
import com.kau.smartbutler.util.fragment.FragmentHistory
import com.kau.smartbutler.view.main.butler.ButlerFragment
import com.kau.smartbutler.view.main.home.HomeFragment
import com.kau.smartbutler.view.main.home.child.MyPageActivity
import com.kau.smartbutler.view.main.home.child.NavClientCenterActivity
import com.kau.smartbutler.view.main.life.LifeFragment
import com.kau.smartbutler.view.main.nav.NavSetDeviceModeActivity
import kotlinx.android.synthetic.main.activity_main_navi.*
import kotlinx.android.synthetic.main.activity_main_content.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.showMyProfileButton
import kotlinx.android.synthetic.main.nav_header_main.view.*

class MainActivity(
        override val layoutRes: Int = R.layout.activity_main_navi,
        override val isUseDatabinding: Boolean = false) :
        BaseActivity(), FragNavController.TransactionListener, FragNavController.RootFragmentListener, View.OnClickListener{

    private var preitem: Int = 0
    lateinit var TABS: Array<String>
    private val REQUIRED_PERMISSIONS =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.INTERNET)
    private val PERMISSIONS_REQUEST_CODE = 100
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // OS가 Marshmallow 이상일 경우 권한체크를 해야 합니다.
            val permissionCheckCamera
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            val permissionCheckStorage
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if (permissionCheckCamera ==
                    PackageManager.PERMISSION_GRANTED && permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {
                // 권한 있음
            } else {
                // 권한 없음
                ActivityCompat.requestPermissions(this,
                        REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE)
            }
        } else {
            // OS가 Marshmallow 이전일 경우 권한체크를 하지 않는다.
            Log.d("MyTag", "마시멜로 버전 이하로 권한 이미 있음")
        }


        setDrawaerWithNavigationView()

        buildView(savedInstanceState)

        setToolbar()

        setClickListener()

        setHeader()
    }

    private fun setDrawaerWithNavigationView(){


        val toggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

    }

    private fun setToolbar(){

        supportActionBar!!.setDisplayShowTitleEnabled(false);

    }

    private fun setHeader(){

        navView.getHeaderView(0).apply {

            showMyProfileButton.setOnClickListener(this@MainActivity)
            naviBackButton.setOnClickListener(this@MainActivity)
            setModeNavTextView.setOnClickListener(this@MainActivity)

        }

    }

    private fun setClickListener(){

        clientCenterNavTextView.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v?.id){

            R.id.clientCenterNavTextView -> startActivity(Intent(this, NavClientCenterActivity::class.java))
            R.id.showMyProfileButton -> startActivity(Intent(this, MyPageActivity::class.java))
            R.id.setModeNavTextView -> startActivity(Intent(this, NavSetDeviceModeActivity::class.java))

        }
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

            FragNavController.TAB1 -> return HomeFragment.getInstance()
            FragNavController.TAB2 -> return ButlerFragment.getInstance()
            FragNavController.TAB3 -> return LifeFragment.getInstance()

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
