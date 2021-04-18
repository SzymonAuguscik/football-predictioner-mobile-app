package com.example.footballpredictioner


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.squareup.picasso.Picasso

class ChosenLeagueActivity : AppCompatActivity() {

    private lateinit var chosenLeagueLogo: ImageView
    private lateinit var tableButton: Button
    private lateinit var predictButton: Button
    private lateinit var scheduleButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_league)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tableButton = findViewById(R.id.table_button)
        predictButton = findViewById(R.id.predict_button)
        scheduleButton = findViewById(R.id.schedule_button)


        /* read intent from MainActivity */
        val chosenLeagueId = intent.getLongExtra("chosenLeagueId", -1)
        val chosenLeagueName = intent.getStringExtra("chosenLeagueName")
        val chosenLeagueLogoUrl = intent.getStringExtra("chosenLeagueLogoUrl")
        val chosenLeagueLastSeasonId = intent.getIntExtra("lastSeasonId",-1)
        val chosenLeagueLastSeasonString = intent.getStringExtra("lastSeasonString")
        val predictions = intent.getStringExtra("predictions")


        /* Load image by url using Picasso lib*/
        chosenLeagueLogo = findViewById(R.id.chosen_league_logo)
        Picasso.get().load(chosenLeagueLogoUrl).into(chosenLeagueLogo)


        tableButton.setOnClickListener {
            val tableIntent  = Intent(this, TableActivity::class.java)
            tableIntent.putExtra("chosenLeagueId", chosenLeagueId)
            tableIntent.putExtra("chosenLeagueName", chosenLeagueName)
            tableIntent.putExtra("chosenLeagueLogoUrl", chosenLeagueLogoUrl)
            tableIntent.putExtra("chosenLeagueLastSeasonId", chosenLeagueLastSeasonId)
            tableIntent.putExtra("chosenLeagueLastSeasonString", chosenLeagueLastSeasonString)
            startActivity(tableIntent)
        }
        predictButton.setOnClickListener {
            val predictIntent  = Intent(this, PredictActivity::class.java)
            predictIntent.putExtra("predictions", predictions)
            predictIntent.putExtra("logoUrl", chosenLeagueLogoUrl)
            startActivity(predictIntent)
        }
        scheduleButton.setOnClickListener {
            val scheduleIntent  = Intent(this, ScheduleActivity::class.java)
            startActivity(scheduleIntent)
        }


    }
}

