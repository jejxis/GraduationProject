package oasis.team.econg.graduationproject.data

data class UserDto(
    val email: String,
    val nickName: String,
    val picture: String
)
data class PwDto(
    val currentPW: String,
    val newPW: String
)