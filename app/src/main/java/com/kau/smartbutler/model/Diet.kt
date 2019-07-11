package com.kau.smartbutler.model

import io.realm.RealmObject

open class Diet(
        var year: Long = 0,
        var month: Long = 0,
        var day: Long = 0
) : RealmObject()