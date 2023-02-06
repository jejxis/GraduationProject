package oasis.team.econg.graduationproject.utils

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import oasis.team.econg.graduationproject.data.JournalsResponseDto
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

fun JsonElement.convertToJournalsResponseDto(): JournalsResponseDto{
    val resultItemObject = this.asJsonObject
    val id = resultItemObject.get("id").asLong
    val content = resultItemObject.get("content").asString
    val picture = if(resultItemObject.get("picture") != JsonNull.INSTANCE) resultItemObject.get("picture").asString else null
    val date = resultItemObject.get("date").asString

    return JournalsResponseDto(
        id = id,
        content = content,
        picture = picture,
        date = date
    )
}
