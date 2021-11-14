package project.android.footstamp.utils

import android.graphics.Bitmap

data class PostModel(

    val area: String = "",
    val district: String = "",
    val time: String = "",
    val memo: String = "",
    val key: String = "",
    var bitmap: Bitmap? = null
        )