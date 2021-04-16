package com.example.footballpredictioner.models

data class MatchModel(val id:Long, val roundId:Long?, val localTeamId:Long,
                      val visitorTeamId:Long, val localTeamScore:Int, val visitorTeamScore:Int, val date:String)