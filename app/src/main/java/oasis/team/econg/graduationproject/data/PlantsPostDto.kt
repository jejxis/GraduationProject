package oasis.team.econg.graduationproject.data

import java.io.Serializable

data class PlantsPostDto(
    val name: String,
    val waterAlarmInterval: Int,
    val waterSupply: String,
    val sunshine: Double,
    val highTemperature: Int,
    val lowTemperature: Int
)
data class DetailToMineDto(
    val water: String,
    val sunshine: String,
    val temperature: String
):Serializable
