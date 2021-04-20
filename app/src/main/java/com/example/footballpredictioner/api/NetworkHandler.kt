package com.example.footballpredictioner.api


import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.footballpredictioner.models.LeagueModel


class NetworkHandler(context: Context) {

    init {
        TemporaryDataHolder.prepare(context)
    }

    companion object{
        const val SCHEME = "https"
        const val AUTHORITY = "soccer.sportmonks.com"
        const val API_PATH = "api"
        const val API_VERSION = "v2.0"
        const val API_TOKEN = "Mlxf90t5AvoYOzgVe7Tj9oKa91X9Sbbx7oyxBMIVmsl8arL0XyRVOoOR0Gis"
//        const val CURRENT_PREMIERSHIP_SEASON_ID = "17141"
//        const val CURRENT_SUPERLIGA_SEASON_ID = "17328"
//        const val PLAYOFFS_PREMIERSHIP_SEASON_ID = "null"
//        const val PLAYOFFS_SUPERLIGA_SEASON_ID = "18101"

    }

    fun sendRequestForTeams(league:LeagueModel, seasonId:String){

         val teamsUrl = Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
             .appendPath(API_PATH)
             .appendPath(API_VERSION)
             .appendPath("teams")
             .appendPath("season")
             .appendPath(seasonId)
             .appendQueryParameter("api_token", API_TOKEN).build().toString()


        val teamsJsonObjReq = JsonObjectRequest(Request.Method.GET, teamsUrl, null,
                { response ->
                    TemporaryDataHolder.handleTeamsResponse(response, league)
                },
                { error -> println("Error occurred - status: ${error?.message}") }
        )

        TemporaryDataHolder.add(teamsJsonObjReq)

    }


    fun sendRequestForRounds(seasonId: String){

        val roundsUrl = Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
                .appendPath(API_PATH)
                .appendPath(API_VERSION)
                .appendPath("rounds")
                .appendPath("season")
                .appendPath(seasonId)
                .appendQueryParameter("api_token", API_TOKEN).build().toString()

        println(roundsUrl)


        val roundsJsonObjReq = JsonObjectRequest(Request.Method.GET, roundsUrl, null,
                { response -> TemporaryDataHolder.handleRoundsResponse(response) },
                { error -> println("Error occurred - status: ${error?.message}") }
        )

        TemporaryDataHolder.add(roundsJsonObjReq)
    }


    private fun sendRequestForMatches(leagueId:String){


        val borderDates = TemporaryDataHolder.dataBaseHelper.getSeasonBorderDates()


        val matchesOfSeasonUrl= Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
                        .appendPath(API_PATH)
                        .appendPath(API_VERSION)
                        .appendPath("fixtures")
                        .appendPath("between")
                        .appendPath(borderDates?.first)
                        .appendPath(borderDates?.second)
                        .appendQueryParameter("api_token", API_TOKEN)
                        .appendQueryParameter("leagues", leagueId)


        val matchJsonObjReq = JsonObjectRequest(Request.Method.GET, matchesOfSeasonUrl.build().toString(), null,
                { response -> TemporaryDataHolder.handleMatchResponse(response, matchesOfSeasonUrl) },
                { error -> println("Error occurred - status: ${error?.message}") }
        )

        TemporaryDataHolder.add(matchJsonObjReq)

    }

}