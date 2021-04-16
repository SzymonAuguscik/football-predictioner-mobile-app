package com.example.footballpredictioner.api

import android.content.Context
import android.net.Uri
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley.newRequestQueue
import com.example.footballpredictioner.database.DataBaseHelper
import com.example.footballpredictioner.models.MatchModel
import com.example.footballpredictioner.models.RoundModel
import com.example.footballpredictioner.models.TeamModel
import org.json.JSONObject

object TemporaryDataHolder {

    private lateinit var queue: RequestQueue
    lateinit var dataBaseHelper: DataBaseHelper


    fun prepare(context: Context){
        queue = newRequestQueue(context)
        dataBaseHelper = DataBaseHelper(context)
    }

    fun add(request: JsonObjectRequest){
        queue.add(request)
    }

    fun handleTeamsResponse(response:JSONObject, leagueName:String){
        response.let {
            val teams = response.getJSONArray("data")
            val teamsCount = teams.length()


            for (i in 0 until teamsCount){
                val team = teams.get(i) as JSONObject
                val teamId = team.getInt("id")
                val teamName = team.getString("name")
                val teamLogoPath = team.getString("logo_path")
                val teamVenueId = team.getInt("venue_id")
                val seasonId = team.getInt("current_season_id")

                val teamModel = TeamModel(teamId.toLong(), teamName, teamLogoPath, leagueName, teamVenueId.toString(), seasonId.toString())

                fetchTeamStatistics(teamId,seasonId ,teamModel)


            }
        }
    }

    private fun fetchTeamStatistics(teamId: Int, seasonId:Int, teamModel: TeamModel){

        val teamStatsUrl = Uri.Builder().scheme(NetworkHandler.SCHEME).authority(NetworkHandler.AUTHORITY)
            .appendPath(NetworkHandler.API_PATH)
            .appendPath(NetworkHandler.API_VERSION)
            .appendPath("teams")
            .appendPath(teamId.toString())
            .appendQueryParameter("include", "stats")
            .appendQueryParameter("seasons", seasonId.toString())
            .appendQueryParameter("api_token", NetworkHandler.API_TOKEN).toString()


        val teamStatsJsonObjReq = JsonObjectRequest(
            Request.Method.GET, teamStatsUrl, null,
            { response -> handleTeamStatsResponse(response, teamModel) },
            { error -> println("Error occurred - status: ${error?.message}") }
        )

        add(teamStatsJsonObjReq)

    }


    private fun handleTeamStatsResponse(response: JSONObject, teamModel: TeamModel) {
        response.let {
            val data = response.getJSONObject("data")
            val statsData = data.getJSONObject("stats").getJSONArray("data").get(0) as JSONObject

            val wins = statsData.getJSONObject("win").getInt("total")
            val loses = statsData.getJSONObject("lost").getInt("total")
            val draws = statsData.getJSONObject("draw").getInt("total")
            val goalsFor = statsData.getJSONObject("goals_for").getInt("total")
            val goalsAgainst = statsData.getJSONObject("goals_against").getInt("total")

            teamModel.wins = wins
            teamModel.draws = draws
            teamModel.loses = loses
            teamModel.goalsFor = goalsFor
            teamModel.goalsAgainst = goalsAgainst

            dataBaseHelper.addNewTeam(teamModel)

        }

    }


    fun handleRoundsResponse(response: JSONObject){

        response.let {
            val rounds = response.getJSONArray("data")
            val roundsCount = rounds.length()


            for (i in 0 until roundsCount){
                val round = rounds.get(i) as JSONObject
                val roundId = round.getInt("id")
                val leagueId = round.getInt("league_id")
                val seasonId= round.getInt("season_id")
                val roundNum= round.getInt("name")
                val roundStartDate = round.getString("start")
                val roundEndDate = round.getString("end")


                val roundModel = RoundModel(roundId.toLong(),roundNum, leagueId.toLong(), seasonId.toLong(),roundStartDate,roundEndDate)

                dataBaseHelper.addNewRound(roundModel)


            }
        }
    }

    fun handleMatchResponse(response: JSONObject, matchesUrl:Uri.Builder){


        response.let {

            handleMatchNextPage(response)

            val metaInfo = response.getJSONObject("meta")
            val pagination = metaInfo.getJSONObject("pagination")
            val totalPages = pagination.getInt("total_pages")


            for (i in 2 until totalPages+1){


                val nextPageUrl = "${matchesUrl.build()}&page=${i}"

                val matchJsonObjReq = JsonObjectRequest(Request.Method.GET, nextPageUrl, null,
                        { response -> handleMatchNextPage(response) },
                        { error -> println("Error occurred - status: ${error?.message}") }
                )

                add(matchJsonObjReq)
            }

        }
    }

    private fun handleMatchNextPage(response: JSONObject?) {

        response?.let {

            val matchesData = response.getJSONArray("data")
            val matchesDataCount = matchesData.length()

            for (i in 0 until matchesDataCount) {

                val match = matchesData.get(i) as JSONObject

                val matchId = match.getLong("id")
                val roundId = if (match.isNull("round_id")) null else match.getLong("round_id")
                val localTeamId = match.getLong("localteam_id")
                val visitorTeamId = match.getLong("visitorteam_id")
                val scores = match.getJSONObject("scores")
                val localTeamScore = scores.getInt("localteam_score")
                val visitorTeamScore = scores.getInt("visitorteam_score")
                val time = match.getJSONObject("time").getJSONObject("starting_at")
                val date = time.getString("date")

                val matchModel = MatchModel(matchId, roundId, localTeamId, visitorTeamId, localTeamScore, visitorTeamScore, date)

                dataBaseHelper.addNewMatch(matchModel)

            }
        }
    }

}