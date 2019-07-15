package com.kau.smartbutler.model

import io.realm.RealmObject

open class Diet(
        var year: Int = 0,
        var month: Int = 0,
        var day: Int = 0
) : RealmObject()