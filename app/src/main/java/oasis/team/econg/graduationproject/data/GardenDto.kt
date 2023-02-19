package oasis.team.econg.graduationproject.data

data class GardenDto(
    val id: Long,
    val name: String,
    val picture: String,
    val manageLevel: String
)

data class GardenDetailDto(
    val id: Long,
    val name: String,
    val picture: String,
    val temperature: String,
    val humidity: String,
    val adviceInfo: String,
    val manageLevel: String,
    val waterSupply: String,
    val light: String,
    val place: String,
    val bug: String,
    val bookmark: Boolean
)