package com.kau.smartbutler.base
import android.app.Application
import androidx.lifecycle.AndroidViewModel

open class BaseViewModel(app: Application) : AndroidViewModel(app) {

    // 토스트 메시지
    var TAG = app.applicationContext.toString()
    var toastMessageText = ""

    override fun onCleared() {
        super.onCleared()
    }
}