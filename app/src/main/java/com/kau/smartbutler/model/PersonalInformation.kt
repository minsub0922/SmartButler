package com.kau.smartbutler.model

import io.realm.RealmObject

open class PersonalInformation(
        var age: Int = 30,
        var sex: String = "남",
        var weight: Int = 70,
        var goalWeight: Int = 70,
        var activity: String = "하", // 1: 하, 2: 중, 3: 상
        var requiredCalorie: Int = 2000
) : RealmObject()