package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ChosenTeamActivity : AppCompatActivity() {

//    private val teamId: Long
//    private val teamName: String?
//    private val teamLogoUrl: String?
//    private val teamCurrSeasonId: Long

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chosen_team)
        actionBar?.setDisplayHomeAsUpEnabled(true)



    }
}