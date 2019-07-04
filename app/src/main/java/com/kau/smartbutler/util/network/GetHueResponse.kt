package com.kau.smartbutler.util.network

data class GetHueResponse(
        val link: String,
        val state: String,
        val editable: Boolean,
        val type: String,
        val name: String,
        val label: String,
        val category: String,
        val tags: Array<String>,
        val groupNames: Array<String>

)

//{"link":"http://192.168.1.48:8080/rest/items/hue_0210_0017881c8274_5_color",
//    "state":"46,56,100","editable":false,
//    "type":"Color",
//    "name":"hue_0210_0017881c8274_5_color",
//    "label":"Color",
//    "category":"ColorLight",
//    "tags":["Lighting"],
//    "groupNames":[]
//}