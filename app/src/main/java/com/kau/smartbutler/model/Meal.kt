package com.kau.smartbutler.model

import com.kau.smartbutler.util.camera.PIC_FILE_NAME
import io.realm.RealmObject
import java.sql.Date

open class Meal(
        var date: Long = 0,
        var type: String = "breakfast", // breakfast, lunch, dinner, etc
        var fileDir: String = PIC_FILE_NAME,
        var foodName: String = "음식",
        var year: Long = 0,
        var month: Long = 0,
        var day: Long = 0,
        var carbo: Float = 0.toFloat(),
        var protein: Float = 0.toFloat(),
        var fat: Float = 0.toFloat()
) : RealmObject()