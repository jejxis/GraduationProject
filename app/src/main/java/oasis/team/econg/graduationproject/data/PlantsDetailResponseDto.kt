package oasis.team.econg.graduationproject.data

data class PlantsDetailResponseDto(
    val name: String,
    val picture: String,
    val adoptingDate: String,
    val waterAlarmInterval: Int,
    val waterSupply: String,
    val sunshine: Double,
    val highTemperature: Int,
    val lowTemperature: Int
)
