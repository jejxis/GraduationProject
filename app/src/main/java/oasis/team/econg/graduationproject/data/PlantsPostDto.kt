package oasis.team.econg.graduationproject.data

data class PlantsPostDto(
    val name: String,
    val waterAlarmInterval: Int,
    val waterSupply: String,
    val sunshine: Int,
    val highTemperature: Int,
    val lowTemperature: Int
)
