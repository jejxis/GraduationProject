package oasis.team.econg.graduationproject.data

import oasis.team.econg.graduationproject.R

data class Plant(
    var plantId: String = "",
    var name: String = "",
    var temp:Double = 0.0,
    var hum:Double = 0.0,
    var days:Int = 0,
    var thumb: Int = R.drawable.ic_baseline_eco_45
)
data class DiaryPlant(
    var plantId: String = "",
    var name: String = "",
    var thumb: Int = R.drawable.ic_baseline_eco_45,
    var latestDiary: String="0000.00.00",
    var waterCheck: Boolean
    )
data class Diary(
    var plantId: String,
    var date: String,
    var whatDay: String,
    var content: String,
    var img: Int = R.drawable.ic_baseline_eco_45
)
data class Schedule(
    var plantId: String,
    var plantName: String,
    var water: Boolean
)
data class PlantSpecies(
    var no: String,
    var plantSpeciesName: String,
    var plantSpeciesPicture: String
)