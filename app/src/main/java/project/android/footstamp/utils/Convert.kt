package project.android.footstamp.utils

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore

fun uriToBitmap(contentResolver: ContentResolver, uri: Uri, size: Int) : Bitmap {
    val origin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver, uri))
    } else {
        MediaStore.Images.Media.getBitmap(contentResolver, uri)
    }
    return Bitmap.createScaledBitmap(origin.copy(Bitmap.Config.ARGB_8888, true), size, size, false)
}