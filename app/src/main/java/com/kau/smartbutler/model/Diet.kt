package com.kau.smartbutler.model

import io.realm.RealmObject

open class Diet(
        var year: Long = 0,
        var month: Long = 0,
        var date: Long = 0,
        var carbo: Float = 0.toFloat(),
        var protein: Float = 0.toFloat(),
        var fat: Float = 0.toFloat()
) : RealmObject()