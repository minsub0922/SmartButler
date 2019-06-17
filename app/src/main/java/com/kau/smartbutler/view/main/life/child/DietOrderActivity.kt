package com.kau.smartbutler.view.main.life.child

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kau.smartbutler.R
import com.kau.smartbutler.base.BaseActivity
import kotlinx.android.synthetic.main.activity_diet.*
import kotlinx.android.synthetic.main.activity_diet_order.*
import kotlinx.android.synthetic.main.activity_pill_diet_apply.*

class DietOrderActivity(
        override val layoutRes: Int=R.layout.activity_diet_order,
        override val isUseDatabinding: Boolean=false) :
        BaseActivity() {

    override var isChildActivity: Boolean=true

    override fun setupView() {
        super.setupView()

        webview.settings.javaScriptEnabled = true
        webview.settings.loadWithOverviewMode = true
        webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
        webview.loadUrl("https://www.naver.com")
    }
}
