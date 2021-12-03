package project.android.footstamp.utils

import android.graphics.Bitmap
import android.graphics.Point
import androidx.annotation.Keep
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.math.pow
import kotlin.random.Random

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
) {
    private var areaPoint: Point? = null
    private var districtPoint: Point? = null

    fun getPointForArea(w: Int, h: Int, cx: Float, cy: Float): Point {
        if (areaPoint == null) {
            var adjX = Random.nextInt(-100, 100)
            var adjY = Random.nextInt(-100, 100)
            while (adjX.toDouble().pow(2.0) + adjY.toDouble().pow(2) < 10000) {
                adjX = Random.nextInt(-100, 100)
                adjY = Random.nextInt(-100, 100)
            }
            val areaCoord = coordMap[area]
            val x = (cx + w*areaCoord!!.first).toInt() + adjX - bitmap!!.width/2
            val y = (cy + h*areaCoord.second).toInt() + adjY - bitmap!!.height/2
            areaPoint = Point(x, y)
        }
        return areaPoint!!
    }
    
    fun getPointForDistrict(w: Int, h: Int, cx: Float, cy: Float): Point {
        if (districtPoint == null) {
            var adjX = Random.nextInt(-100, 100)
            var adjY = Random.nextInt(-100, 100)
            while (adjX.toDouble().pow(2.0) + adjY.toDouble().pow(2) < 10000) {
                adjX = Random.nextInt(-100, 100)
                adjY = Random.nextInt(-100, 100)
            }
            val areaCoord = coordMap[district]
            val x = (cx + w*areaCoord!!.first).toInt() + adjX - bitmap!!.width/2
            val y = (cy + h*areaCoord.second).toInt() + adjY - bitmap!!.height/2
            districtPoint = Point(x, y)
        }
        return districtPoint!!
    }
}