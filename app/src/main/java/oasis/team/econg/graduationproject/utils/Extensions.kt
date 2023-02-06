package oasis.team.econg.graduationproject.utils

import com.google.gson.JsonElement
import oasis.team.econg.graduationproject.data.PlantsResponseDto

fun String?.isJsonObject():Boolean {
    if(this == null) return false
    else return this!!.startsWith("{") == true && this!!.endsWith("}")
}

fun String?.isJsonArray():Boolean {
    if(this == null) return false
    else return this!!.startsWith("[") == true && this!!.endsWith("]")
}

fun JsonElement.convertToPlantsResponseDto(): PlantsResponseDto{
    val resultItemObject = this.asJsonObject
    val id = resultItemObject.get("id").asLong
    val name = resultItemObject.get("name").asString
    val picture = resultItemObject.get("picture").asString
    val recentRecordDate = resultItemObject.get("recentRecordDate").asString
    val dday = resultItemObject.get("dday").asLong

    return PlantsResponseDto(
        id = id,
        name = name,
        picture = picture,
        recentRecordDate = recentRecordDate,
        dday = dday
    )
}
