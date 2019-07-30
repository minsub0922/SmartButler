package com.kau.smartbutler.model

data class PostDetectionAreaRequest(
        var Intrusion:String,
        var Loitering:String,
        var Abandon:String,
        var Falldown:String
)