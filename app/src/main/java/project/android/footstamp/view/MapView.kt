package project.android.footstamp.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import project.android.footstamp.R
import project.android.footstamp.utils.PostModel


class MapView(context: Context, attrs: AttributeSet): View(context, attrs) {
    private lateinit var extraCanvas: Canvas
    private lateinit var extraBitmap: Bitmap
    private var postList: List<PostModel> = listOf()
    private val imageBitmap = BitmapFactory.decodeResource(resources, R.drawable.map)
    private val paint: Paint = Paint().apply { colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.purple_500), PorterDuff.Mode.SRC_IN) }

    private var area = "all"

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if(::extraBitmap.isInitialized) extraBitmap.recycle()
        val factor = w.toDouble() / imageBitmap.width
        extraBitmap = Bitmap.createScaledBitmap(imageBitmap, w, (imageBitmap.height * factor).toInt(), true)
        extraCanvas = Canvas(extraBitmap)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val centerX = (width - extraBitmap.width) / 2f
        val centerY = (height - extraBitmap.height) / 2f
        paint.color = 0xff424242.toInt()
        canvas.drawBitmap(extraBitmap, centerX, centerY, paint)


        postList.forEach {
            val point = if (area == "all") it.getPointForArea(width, height) else it.getPointForDistrict(width, height)

            canvas.drawBitmap(getBubbleBitmap(it), point.x.toFloat(), point.y.toFloat(), Paint())
        }
    }

    fun setArea(area: String) { this.area = area }
    public fun setPostList(list: List<PostModel>) {
        Log.d("MapView", "postList: ${postList.size} list: ${list.size}")
        postList = listOf()
        postList = list
        invalidate()
    }

    fun getBubbleBitmap(post: PostModel): Bitmap {
        var image: Bitmap = post.bitmap!!
        var output: Bitmap = Bitmap.createBitmap(image.width, image.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, image.width, image.height)
        paint.isAntiAlias = true;
        canvas.drawARGB(0, 0, 0, 0);
        paint.color = 0xff424242.toInt()
        canvas.drawCircle(image.width/2f, image.height/2f, image.width/2f, paint)
        // Draw the image
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(image, rect, rect, paint)
        // Draw the createBitmapWithBorder
//        paint.xfermode = null
//        paint.style = Paint.Style.STROKE
//        paint.strokeWidth = 10f
//        canvas.drawCircle(image.width/2f, image.height/2f, image.width/2f+10, paint)
        return output
    }

}