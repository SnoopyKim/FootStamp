package project.android.footstamp.utils

val listOfArea: Map<String, List<String>> = mapOf(
    "전체" to listOf("전체"),
    "동부" to listOf("전체","강동구", "광진구", "동대문구", "성동구", "중랑구"),
    "서부" to listOf("전체","강서구", "구로구", "금천구", "양천구", "영등포구"),
    "남부" to listOf("전체","강남구", "관악구", "동작구", "서초구", "송파구"),
    "북부" to listOf("전체","강북구", "노원구", "도봉구", "성북구"),
    "중부" to listOf("전체","마포구", "서대문구", "용산구", "은평구", "종로구", "중구")
)

fun getAreas(): List<String> = listOfArea.keys.toList()

fun getDistrictsFromArea(area: String): List<String> = listOfArea[area] ?: listOf()

fun getAreaFromDistrict(district: String): String = listOfArea.filter { it.value.contains(district) }.keys.first()