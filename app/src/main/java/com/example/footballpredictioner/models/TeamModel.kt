package com.example.footballpredictioner.models

data class TeamModel(val id:Long, val name:String, val logoPath:String,
                     val leagueName: String, var venueName:String = "", var season:String = "",
                     var wins:Int = -1, var loses:Int = -1 , var draws:Int = -1,
                     var goalsFor:Int = -1, var goalsAgainst:Int = -1, )