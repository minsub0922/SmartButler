package com.kau.smartbutler.model

import io.realm.RealmObject

open class Calorie(
        var data: Long = 0,
        var carbohydrate: Float = 0.toFloat(),
        var protein: Float = 0.toFloat(),
        var fat: Float = 0.toFloat()
) : RealmObject()