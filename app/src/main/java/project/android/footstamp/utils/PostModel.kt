package project.android.footstamp.utils

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
data class PostModel(

    val area : String = "",
    val district : String = "",
    val time : String = "",
    val memo : String = "",
    val key : String = "",
    val url : String = "",
    var bitmap : Bitmap? = null
)