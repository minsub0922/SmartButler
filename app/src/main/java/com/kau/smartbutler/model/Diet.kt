package com.kau.smartbutler.model

import io.realm.RealmObject

open class Diet(
        var year: Int = 0,
        var month: Int = 0,
        var date: Int = 0,
        var carbo: Float = 0.toFloat(),
        var protein: Float = 0.toFloat(),
        var fat: Float = 0.toFloat()
) : RealmObject()