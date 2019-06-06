package com.kau.smartbutler.viewModel

import android.app.Application
import androidx.databinding.ObservableField
import com.kau.smartbutler.base.BaseViewModel

class LifeViewModel(app: Application) : BaseViewModel(app) {

    val fallState = ObservableField<Boolean>()


}