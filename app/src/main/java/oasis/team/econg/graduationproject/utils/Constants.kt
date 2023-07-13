package oasis.team.econg.graduationproject.utils

import oasis.team.econg.graduationproject.R
import oasis.team.econg.graduationproject.samplePreference.MyApplication

object Constants {
    const val BASE_URL = ""
    const val TAG : String = "MYTAG"
    const val GUIDELINE: String = "https://smore.im/quiz/vbqDYbbjPT"//"https://doda.app/quiz/jjc3lfdvlX"
}

object CultureSettings{
    val WATER_ARRAY = arrayOf(
        "관수 양을 선택하세요.",
        "화분 흙 대부분 말랐을때 충분히 관수함",
    "토양 표면이 말랐을때 충분히 관수함",
    "흙을 촉촉하게 유지함(물에 잠기지 않도록 주의)",
    "항상 흙을 축축하게 유지함(물에 잠김)")

    val SUNSHINE_ARRAY = arrayOf(
        "일조량을 선택하세요.",
        "낮은 광도(300~800 Lux)",
        "중간 광도(800~1,500 Lux)",
        "높은 광도(1,500~10,000 Lux)"
    )
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL,
    NOT_FOUND,
    EXIST_USER,
    PASSWORD_NOT_MATCH
}

object API{
    const val BASE_URL : String = ""
    //    const val HEADER_TOKEN : String = ""
    //var HEADER_TOKEN : String = "${MyApplication.prefs.token}"
}

val skyList = arrayOf("", "맑음", "", "구름많음", "흐림")
val ptyList = arrayOf("없음", "비", "비/눈", "눈","", "비", "눈", "눈")
val weatherIconSetMap: Map<String, Int> = mapOf(
    "맑음" to R.drawable.weather_sunny,
    "구름많음" to R.drawable.weather_some_cloud,
    "흐림" to R.drawable.weather_cloudy,
    "비" to R.drawable.weather_rainy,
    "비/눈" to R.drawable.weather_rain_snow,
    "눈" to R.drawable.weather_snowy
)
