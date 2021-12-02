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

    fun getPointForArea(w: Int, h: Int): Point {
        if (areaPoint == null) {
            var adjX = Random.nextInt(100)
            var adjY = Random.nextInt(100)
            while (adjX.toDouble().pow(2.0) + adjY.toDouble().pow(2) < 10000) {
                adjX = Random.nextInt(-100, 100)
                adjY = Random.nextInt(-100, 100)
            }
            val _w = w - bitmap!!.width + adjX
            val _h = h - bitmap!!.height + adjY
            areaPoint = when (area) {
                "동부" -> Point(_w/5*4, _h / 2)
                "서부" -> Point(_w/5, _h / 2)
                "남부" -> Point(_w / 2, _h/5*4)
                "북부" -> Point(_w / 2, _h/5)
                "중부" -> Point(_w / 2, _h / 2)
                else -> Point(_w / 2, _h / 2)
            }
        }
        return areaPoint!!
    }
    
    fun getPointForDistrict(w: Int, h: Int): Point {
        if (districtPoint == null) {
            var adjX = Random.nextInt(100)
            var adjY = Random.nextInt(100)
            while (adjX.toDouble().pow(2.0) + adjY.toDouble().pow(2) < 100) {
                adjX = Random.nextInt(-200, 200)
                adjY = Random.nextInt(-200, 200)
            }
            val _w = w - bitmap!!.width + adjX
            val _h = h - bitmap!!.height + adjY
            districtPoint =  when (district) {
                "강동구" -> Point(_w / 2, _h / 2)
                "광진구" -> Point(_w / 2, _h / 2)
                "동대문구" -> Point(_w / 2, _h / 2)
                "성동구" -> Point(_w / 2, _h / 2)
                "중랑구" -> Point(_w / 2, _h / 2)
                "강서구" -> Point(_w / 2, _h / 2)
                "구로구" -> Point(_w / 2, _h / 2)
                "금천구" -> Point(_w / 2, _h / 2)
                "양천구" -> Point(_w / 2, _h / 2)
                "영등포구" -> Point(_w / 2, _h / 2)
                "강남구" -> Point(_w / 2, _h / 2)
                "관악구" -> Point(_w / 2, _h / 2)
                "동작구" -> Point(_w / 2, _h / 2)
                "서초구" -> Point(_w / 2, _h / 2)
                "송파구" -> Point(_w / 2, _h / 2)
                "강북구" -> Point(_w / 2, _h / 2)
                "노원구" -> Point(_w / 2, _h / 2)
                "도봉구" -> Point(_w / 2, _h / 2)
                "성북구" -> Point(_w / 2, _h / 2)
                "마포구" -> Point(_w / 2, _h / 2)
                "서대문구" -> Point(_w / 2, _h / 2)
                "용산구" -> Point(_w / 2, _h / 2)
                "은평구" -> Point(_w / 2, _h / 2)
                "종로구" -> Point(_w / 2, _h / 2)
                "중구" -> Point(_w / 2, _h / 2)
                else -> Point(_w / 2, _h / 2)
            }
        }
        return districtPoint!!
    }
}