package com.kau.smartbutler.model

import io.realm.RealmObject

data class PersonalInformation(
        var age: Int = 30,
        var sex: String,
        var weight: Int,
        var goalWeight: Int,
        var activity: Int = 1, // 1: 하, 2: 중, 3: 상
        var requiredCalorie: Int = 2000
) : RealmObject() {

}