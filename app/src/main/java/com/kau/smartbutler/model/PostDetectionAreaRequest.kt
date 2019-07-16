package com.kau.smartbutler.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.ObservableField
import java.util.*

//
//“Intrusion” : “[]”,
//“Loitering” : “[]”,
//“Abandon” : “[]”,
//“Falldown” : “[]”


data class PostDetectionAreaRequest(
        var Intrusion:String,
        var Loitering:String,
        var Abandon:String,
        var Falldown:String
)