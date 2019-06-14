package com.kau.smartbutler.model

import androidx.databinding.ObservableField

data class Device(
        var type:Int,
        var name:String,
        var state:Boolean = true

    //type 0: 기기 추가 / 1:서재조명 / 2:tv / 3:에어컨
)