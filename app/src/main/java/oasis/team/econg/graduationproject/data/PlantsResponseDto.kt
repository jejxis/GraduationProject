package oasis.team.econg.graduationproject.data

data class PlantsResponseDto(
    var id: Long,
    var name: String,
    var picture: String,
    var recentRecordDate: String,
    var star: Boolean,
    var dday: Long
)
