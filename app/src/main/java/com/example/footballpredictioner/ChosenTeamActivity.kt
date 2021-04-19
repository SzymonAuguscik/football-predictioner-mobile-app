package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.squareup.picasso.Picasso

class ChosenTeamActivity : AppCompatActivity() {

    private lateinit var chosenTeamLogo: ImageView
    private lateinit var chosenTeamName: TextView
    private lateinit var lastFiveMatchesList: ListView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_team)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        chosenTeamLogo = findViewById(R.id.chosen_team_logo)
        chosenTeamName = findViewById(R.id.chosen_team_name)
        lastFiveMatchesList = findViewById(R.id.last_five_list)

        val teamId = intent.getLongExtra("teamId",-1)
        val teamName = intent.getStringExtra("teamName")
        val teamLogoUrl = intent.getStringExtra("teamLogoUrl")
        val teamCurrSeasonId = intent.getStringExtra("teamCurrSeasonId")

        Picasso.get().load(teamLogoUrl).into(chosenTeamLogo)
        chosenTeamName.text = teamName

        val chosenTeamLast5Matches = TemporaryDataHolder.dataBaseHelper.getTeamLastFiveMatches(teamId)
        val arrayOfLast5MatchesStrings = arrayListOf<String>()

        chosenTeamLast5Matches.forEach{ match ->
            val localTeam = TemporaryDataHolder.dataBaseHelper.getTeamNameById(match.localTeamId)
            val visitorTeam = TemporaryDataHolder.dataBaseHelper.getTeamNameById(match.visitorTeamId)
            val resultString = "$localTeam  ${match.localTeamScore}:${match.visitorTeamScore}  $visitorTeam"

            arrayOfLast5MatchesStrings.add(resultString)
        }


        val newAdapter = ArrayAdapter(this,R.layout.match_row_in_list,R.id.match_result, arrayOfLast5MatchesStrings)
        lastFiveMatchesList.adapter = newAdapter

        println(arrayOfLast5MatchesStrings.size)



    }
}