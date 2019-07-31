package com.kau.smartbutler.base

import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.kau.smartbutler.R
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity() {

    open var toolbar: Toolbar? = null
    abstract val layoutRes: Int
    abstract val isUseDatabinding: Boolean
    open var isChildActivity:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (layoutRes != 0) {
            if (!isUseDatabinding)
                setContentView(layoutRes)
            else
                onDataBinding()
        }

        try {

            toolbar = findViewById(R.id.toolbar)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayShowTitleEnabled(false);

            if (isChildActivity) {
                supportActionBar?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
                toolbar!!.setNavigationOnClickListener { v -> onBackPressed() }
            }

        }catch (ex : Exception){
            Log.d("tagg"," fail to set Toolbar !!  " + ex)}

        updateStatusBarColor()
        setupView()
        subscribeUI()
    }

    fun updateStatusBarColor () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.statusBarColor = ContextCompat.getColor(this, R.color.AppThemeColor);
        }
    }



    open fun onDataBinding() {

    }

    open fun setupView() {

    }

    open fun subscribeUI() {

    }

    // 다이얼로그 설정

}