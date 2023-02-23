package oasis.team.econg.graduationproject.utils

import com.google.gson.JsonElement
import com.google.gson.JsonNull
import oasis.team.econg.graduationproject.data.*

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

fun JsonElement.convertToDocument(): Document {
    var resultItemObject = this.asJsonObject
    val place_name = resultItemObject.get("place_name").asString
    val phone = resultItemObject.get("phone").asString
    val address_name = resultItemObject.get("address_name").asString
    val road_address_name = resultItemObject.get("road_address_name").asString
    val place_url = resultItemObject.get("place_url").asString
    val x = resultItemObject.get("x").asString
    val y = resultItemObject.get("y").asString

    return Document(
        place_name = place_name,
        phone = phone,
        address_name = address_name,
        road_address_name = road_address_name,
        place_url = place_url,
        x = x,
        y = y
    )
}

fun JsonElement.convertToGardenDto(): GardenDto{
    var resultItemObject = this.asJsonObject

    val id = resultItemObject.get("id").asLong
    val name = resultItemObject.get("name").asString
    val picture = resultItemObject.get("picture").asString
    val manageLevel = resultItemObject.get("manageLevel").asString

    return GardenDto(
        id = id,
        name = name,
        picture = picture,
        manageLevel = manageLevel
    )
}

fun JsonElement.convertToGardenDetailDto(): GardenDetailDto{
    var resultItemObject = this.asJsonObject

    val id = resultItemObject.get("id").asLong
    val name = resultItemObject.get("name").asString
    val picture = resultItemObject.get("picture").asString
    val temperature = resultItemObject.get("temperature").asString
    val humidity = resultItemObject.get("humidity").asString
    val adviceInfo = resultItemObject.get("adviceInfo").asString
    val manageLevel = resultItemObject.get("manageLevel").asString
    val waterSupply = resultItemObject.get("waterSupply").asString
    val light = resultItemObject.get("light").asString
    val place = resultItemObject.get("place").asString
    val bug = resultItemObject.get("bug").asString
    val bookmark = resultItemObject.get("bookmark").asBoolean

    return GardenDetailDto(
        id = id,
        name = name,
        picture = picture,
        temperature = temperature,
        humidity = humidity,
        adviceInfo = adviceInfo,
        manageLevel = manageLevel,
        waterSupply = waterSupply,
        light = light,
        place = place,
        bug = bug,
        bookmark = bookmark
    )
}

fun JsonElement.convertToSchedule(): Schedule{
    var resultItemObject = this.asJsonObject

    val date = resultItemObject.get("date").asString
    val plantName = resultItemObject.get("plantName").asString
    val careType = resultItemObject.get("careType").asString

    return Schedule(
        date = date,
        plantName = plantName,
        careType = careType
    )
}

fun JsonElement.convertToPlantsDetailResponseDto(): PlantsDetailResponseDto{
    var resultItemObject = this.asJsonObject

    val name = resultItemObject.get("name").asString
    val picture = resultItemObject.get("picture").asString
    val adoptingDate = resultItemObject.get("adoptingDate").asString
    val waterAlarmInterval = resultItemObject.get("waterAlarmInterval").asInt
    val waterSupply = resultItemObject.get("waterSupply").asString
    val sunshine = resultItemObject.get("sunshine").asDouble
    val highTemperature = resultItemObject.get("highTemperature").asInt
    val lowTemperature = resultItemObject.get("lowTemperature").asInt

    return PlantsDetailResponseDto(
        name = name,
        picture = picture,
        adoptingDate = adoptingDate,
        waterAlarmInterval = waterAlarmInterval,
        waterSupply = waterSupply,
        sunshine = sunshine,
        highTemperature = highTemperature,
        lowTemperature = lowTemperature
    )
}
