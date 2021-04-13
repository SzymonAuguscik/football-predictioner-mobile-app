package com.example.footballpredictioner

data class TeamModel(val id:Long, val name:String, val logoPath:String,
                     val leagueName:String, val venueId:Long, val seasonId:Long,
                     val wins:Int, val loses:Int, val draws:Int, val points:Int)