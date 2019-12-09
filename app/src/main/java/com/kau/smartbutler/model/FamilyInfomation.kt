package com.kau.smartbutler.model

import io.realm.RealmObject

open class FamilyInfomation(
        var name: String = "가족 이름",
        var relation: String = "부/모/형제/남매/자매",
        var phone: String = "010-0000-0000",
        var email: String = "abc@naver.com",
        var address: String = "서울시 서대문구"
) : RealmObject()