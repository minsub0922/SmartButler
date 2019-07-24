package com.kau.smartbutler.view.main.home.child.cctv


import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class XY(

)

// Data Transfer Object
open class CCTVRealmStruct(
        @PrimaryKey var id: Long = 0,
        var Location : String? = null,  //장소
        var Intrusion : String? = null, //침입
        var Loitering : String? = null, //배회
        var Abandon : String? = null,   //유기
        var Falldown : String? = null   //낙상

        //var date: Date? = null
) : RealmObject()