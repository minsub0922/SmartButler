package com.kau.smartbutler.model

import androidx.databinding.ObservableField

data class Device(
        var type:Int,
        var name:String,
        var state:Boolean = true

)