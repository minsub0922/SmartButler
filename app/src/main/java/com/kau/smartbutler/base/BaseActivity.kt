package com.kau.smartbutler.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kau.smartbutler.R
import java.lang.Exception

abstract class BaseActivity : AppCompatActivity() {

    open var toolbar: Toolbar? = null
    abstract val layoutRes: Int
    abstract val isUseDatabinding: Boolean

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

        }catch (ex : Exception){}

        setupView()
        subscribeUI()
    }



    open fun onDataBinding() {

    }

    open fun setupView() {

    }

    open fun subscribeUI() {

    }

    // 다이얼로그 설정

}