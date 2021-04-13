package com.example.footballpredictioner



import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue


class NetworkHandler(context: Context) {

    private val queue = newRequestQueue(context)
    private val dataBaseHelper = DataBaseHelper(context)

    companion object{
        private const val SCHEME = "https"
        private const val AUTHORITY = "soccer.sportmonks.com"
        private const val API_PATH = "api"
        private const val API_VERSION = "v2.0"
        val BASE_API_URL: Uri.Builder = Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(API_PATH).appendPath(API_VERSION)
        const val API_TOKEN = "Mlxf90t5AvoYOzgVe7Tj9oKa91X9Sbbx7oyxBMIVmsl8arL0XyRVOoOR0Gis"

        const val SEASON_ID = "17141"
    }

    fun sendRequestForLeagueTeams(leagueName:String){

        val teamsUrl = BASE_API_URL
                .appendPath("teams")
                .appendPath("season")
                .appendPath(SEASON_ID)
                .appendQueryParameter("api_token", API_TOKEN).toString()

        val teamsJsonObjReq = JsonObjectRequest(Request.Method.GET, teamsUrl, null,
                { response -> dataBaseHelper.addTeams(response, leagueName)},
                { error -> println("Error occurred - status: ${error?.message}") }
        )

        queue.add(teamsJsonObjReq)


    }



}