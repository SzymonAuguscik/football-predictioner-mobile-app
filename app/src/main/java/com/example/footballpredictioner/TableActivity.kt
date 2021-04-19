package com.example.footballpredictioner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.footballpredictioner.adapters.PredictionAdapter
import com.example.footballpredictioner.adapters.TableAdapter
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.example.footballpredictioner.models.TeamModel
import com.squareup.picasso.Picasso
import java.text.FieldPosition

class TableActivity : AppCompatActivity(), TableAdapter.OnItemClickListener{

    private lateinit var chosenLeagueLogo: ImageView
    private lateinit var teamsInLeagueRecycler: RecyclerView
    private lateinit var currentSeasonYear: TextView
    private lateinit var teamsAdapter: TableAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_table)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        val chosenLeagueName = intent.getStringExtra("chosenLeagueName")
        val chosenLeagueLogoUrl = intent.getStringExtra("chosenLeagueLogoUrl")
        val chosenLeagueLastSeasonId = intent.getIntExtra("chosenLeagueLastSeasonId",-1)
        val chosenLeagueLastSeasonString = intent.getStringExtra("chosenLeagueLastSeasonString")

        chosenLeagueLogo = findViewById(R.id.league_table_logo)
        Picasso.get().load(chosenLeagueLogoUrl).into(chosenLeagueLogo)

        currentSeasonYear = findViewById(R.id.current_season_year)
        currentSeasonYear.text = chosenLeagueLastSeasonString


        val adapterArrayList  = TemporaryDataHolder.dataBaseHelper.getTeamsFromGivenSeason(chosenLeagueLastSeasonId, chosenLeagueName!!)
        teamsAdapter = TableAdapter(adapterArrayList,this,this)
        teamsAdapter.notifyDataSetChanged()

        teamsInLeagueRecycler = findViewById(R.id.league_teams_recycler)
        teamsInLeagueRecycler.layoutManager = LinearLayoutManager(this)
        teamsInLeagueRecycler.adapter = teamsAdapter

    }

    override fun onItemClick(position: Int) {
        println(position)
        val team = teamsAdapter.dataSet[position]
        val intent = Intent(this, ChosenTeamActivity::class.java)

        intent.putExtra("teamId",team.id)
        intent.putExtra("teamName",team.name)
        intent.putExtra("teamLogoUrl",team.logoPath)
        intent.putExtra("teamCurrSeasonId",team.season)

//        val chosenTeamLast5Matches = TemporaryDataHolder.dataBaseHelper.getTeamLastFiveMatches()



        startActivity(intent)
    }


}