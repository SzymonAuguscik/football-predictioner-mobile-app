package com.example.footballpredictioner



import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest


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

    fun sendRequestForTeams(leagueName:String, seasonId:String){

         val teamsUrl = Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
             .appendPath(API_PATH)
             .appendPath(API_VERSION)
             .appendPath("teams")
             .appendPath("season")
             .appendPath(seasonId)
             .appendQueryParameter("api_token", API_TOKEN).build().toString()


        val teamsJsonObjReq = JsonObjectRequest(Request.Method.GET, teamsUrl, null,
                { response -> TemporaryDataHolder.handleTeamsResponse(response, leagueName)},
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


        val roundsJsonObjReq = JsonObjectRequest(Request.Method.GET, roundsUrl, null,
                { response -> TemporaryDataHolder.handleRoundsResponse(response) },
                { error -> println("Error occurred - status: ${error?.message}") }
        )

        TemporaryDataHolder.add(roundsJsonObjReq)
    }


    fun sendRequestForMatches(){

        //Premiership rounds dates
        val borderDates = TemporaryDataHolder.dataBaseHelper.getSeasonBorderDates()
        
        println(borderDates?.first)
        println(borderDates?.second)


        //Premiership teams ids
        val listOfIds = TemporaryDataHolder.dataBaseHelper.getTeamIdsFromGivenSeason("17141")



        TemporaryDataHolder.dataBaseHelper.addNewMatch(1)

//        listOfIds.forEach { teamId ->
//            listOfRoundsDates.forEach { pairOfDates ->
//                val matchDataUrl= Uri.Builder().scheme(SCHEME).authority(AUTHORITY)
//                        .appendPath(API_PATH)
//                        .appendPath(API_VERSION)
//                        .appendPath("fixtures")
//                        .appendPath("between")
//                        .appendPath(pairOfDates.first)
//                        .appendPath(pairOfDates.second)
//                        .appendPath(teamId.toString())
//                        .appendQueryParameter("api_token", API_TOKEN).build().toString()
//
//                val matchJsonObjReq = JsonObjectRequest(Request.Method.GET, matchDataUrl, null,
//                        { response -> TemporaryDataHolder.handleMatchResponse(response) },
//                        { error -> println("Error occurred - status: ${error?.message}") }
//                )
//
//                TemporaryDataHolder.add(matchJsonObjReq)
//
//            }
//        }

    }




}