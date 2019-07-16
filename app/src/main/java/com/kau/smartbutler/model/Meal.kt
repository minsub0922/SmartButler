package com.kau.smartbutler.model

import com.kau.smartbutler.util.camera.PIC_FILE_NAME
import io.realm.RealmObject
import java.sql.Date

open class Meal(
        var date: Long = 0,
        var type: String = "breakfast", // breakfast, lunch, dinner
        var fileDir: String = PIC_FILE_NAME,
        var foodName: String = "음식"
) : RealmObject()