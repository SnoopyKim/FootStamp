package project.android.footstamp.view

import android.content.ContentResolver
import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import project.android.footstamp.R
import project.android.footstamp.utils.PostModel
import project.android.footstamp.utils.uriToBitmap
import kotlin.random.Random


class MapView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.map)
    private val paint: Paint = Paint().apply { colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.purple_500), PorterDuff.Mode.SRC_IN) }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(::extraBitmap.isInitialized) extraBitmap.recycle()
        val factor = w.toDouble() / imageBitmap.width
        extraBitmap = Bitmap.createScaledBitmap(imageBitmap, w, (imageBitmap.height * factor).toInt(), true)
        extraCanvas = Canvas(extraBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(extraBitmap, 0f, 0f, paint)

        canvas.drawText("상일이바보sdsdsdsdsdsdsdsdsdsdsd", 0f, 0f, paint)
        canvas.drawBitmap(getBubbleBitmap(PostModel()), 0f, 0f, Paint())
    }

    fun getBubbleBitmap(postModel: PostModel): Bitmap {
        val randomSize = Random.nextInt(100, 500)
        var image: Bitmap = uriToBitmap(context.contentResolver, Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + context.resources.getResourcePackageName(R.drawable.img)
                + '/' + context.resources.getResourceTypeName(R.drawable.img)
                + '/' + context.resources.getResourceEntryName(R.drawable.img)), randomSize)
        var output: Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, image.width, image.height)
        paint.isAntiAlias = true;
        canvas.drawARGB(0, 0, 0, 0);
        paint.color = 0xff424242.toInt()
        canvas.drawCircle(image.width/2f, image.height/2f, image.width/2f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(image, rect, rect, paint)
        return output
    }
}