package com.kau.smartbutler.model

import androidx.databinding.ObservableField

data class Pill(
        var name:String,
        var component:String,
        var caution: String,
        var sideEffect:String,
        var recommendSymptom: String,
        var recommendFood: String,
        var recommendDiet: String

    //type 0: 병원 / 1:택시
)