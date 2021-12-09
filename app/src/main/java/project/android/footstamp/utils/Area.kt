package project.android.footstamp.utils

val listOfArea: Map<String, List<String>> = mapOf(
    "지역" to listOf("구역"),
    "동부" to listOf("구역","강동구", "광진구", "동대문구", "성동구", "중랑구"),
    "서부" to listOf("구역","강서구", "구로구", "금천구", "양천구", "영등포구"),
    "남부" to listOf("구역","강남구", "관악구", "동작구", "서초구", "송파구"),
    "북부" to listOf("구역","강북구", "노원구", "도봉구", "성북구"),
    "중부" to listOf("구역","마포구", "서대문구", "용산구", "은평구", "종로구", "중구")
)

fun getAreas(): List<String> = listOfArea.keys.toList()

fun getDistrictsFromArea(area: String): List<String> = listOfArea[area] ?: listOf()

fun getAreaFromDistrict(district: String): String = listOfArea.filter { it.value.contains(district) }.keys.first()

val coordMap: Map<String, Pair<Double, Double>> = mapOf(
    "동부" to Pair(0.33, -0.01),
    "서부" to Pair(-0.28, 0.17),
    "남부" to Pair(0.16, 0.34),
    "북부" to Pair(0.16, -0.28),
    "중부" to Pair(-0.10, -0.04),
    "강동구" to Pair(0.31, 0.14),
    "광진구" to Pair(-0.03, 0.26),
    "동대문구" to Pair(-0.25, -0.17),
    "성동구" to Pair(-0.33, 0.20),
    "중랑구" to Pair(0.05, -0.29),
    "강서구" to Pair(-0.23, -0.27),
    "구로구" to Pair(-0.04, 0.18),
    "금천구" to Pair(0.25, 0.38),
    "양천구" to Pair(0.00, 0.00),
    "영등포구" to Pair(0.31, 0.00),
    "강남구" to Pair(0.11, -0.05),
    "관악구" to Pair(-0.33, 0.23),
    "동작구" to Pair(-0.31, -0.21),
    "서초구" to Pair(-0.03, 0.18),
    "송파구" to Pair(0.34, -0.14),
    "강북구" to Pair(-0.28, 0.00),
    "노원구" to Pair(0.23, -0.06),
    "도봉구" to Pair(-0.10, -0.29),
    "성북구" to Pair(-0.19, 0.36),
    "마포구" to Pair(-0.27, 0.21),
    "서대문구" to Pair(-0.02, 0.07),
    "용산구" to Pair(0.23, 0.39),
    "은평구" to Pair(-0.12, -0.21),
    "종로구" to Pair(0.25, -0.05),
    "중구" to Pair(0.32, 0.17),

    )
