package com.kau.smartbutler.model

import androidx.databinding.ObservableField

data class Hospital(
        var type:Int,
        var name:String,
        var address:String,
        var number: String

    //type 0: 병원 / 1:택시
)