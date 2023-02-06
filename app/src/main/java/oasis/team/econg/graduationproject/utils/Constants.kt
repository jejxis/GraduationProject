package oasis.team.econg.graduationproject.utils

import oasis.team.econg.graduationproject.samplePreference.MyApplication

object Constants {
    const val BASE_URL = "https://oasisserver.site"
    const val TAG : String = "MYTAG"
}

object CultureSettings{
    val WATER_ARRAY = arrayOf("관수 양을 선택하세요.",
        "화분 흙 대부분 말랐을 때 충분히 관수함",
    "토양 표면이 말랐을 때 충분히 관수함.",
    "흙을 촉촉하게 유지함(물에 잠기지 않도록 주의)",
    "항상 흙을 축축하게 유지함(물에 잠김)")
}

enum class RESPONSE_STATE{
    OKAY,
    FAIL
}

object API{
    const val BASE_URL : String = "https://oasisserver.site"
    //    const val HEADER_TOKEN : String = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJoZWxsb0BnbWFpbC5jb20iLCJhdXRoIjoiUk9MRV9VU0VSIiwiZXhwIjoxNjY1MDc2MTk2fQ.WPuvT-YsumY2xQsmwSCHFzIQbgCgthdHEGQ0qT0UXyFeb_QkpC1FZthgeVRlrSEmncqwcgq_Fi-XxO9zN4GXhw"
    var HEADER_TOKEN : String = "Bearer ${MyApplication.prefs.token}"
}