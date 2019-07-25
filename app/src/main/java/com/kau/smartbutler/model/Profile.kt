package com.kau.smartbutler.model

import io.realm.RealmObject

open class Profile (
        var name: String = "홍길동",
        var phone: String = "010-0000-0000",
        var email: String = "test@naver.com",
        var address: String = "서울시 서대문구",
        var cctvIP: String = "112.169.29.116", // 1: 하, 2: 중, 3: 상
        var openhapIP: String = "112.169.29.116"
) : RealmObject()