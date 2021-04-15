package com.example.footballpredictioner

data class MatchModel(val id:Long, val roundId:Long?, val localTeamId:Long,
                      val visitorTeamId:Long, val localTeamScore:Int, val visitorTeamScore:Int, val date:String)