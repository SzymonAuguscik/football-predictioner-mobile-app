package com.example.footballpredictioner

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.chaquo.python.Python
import com.example.footballpredictioner.api.NetworkHandler
import com.example.footballpredictioner.api.TemporaryDataHolder
import com.example.footballpredictioner.models.LeagueModel
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var networkHandler: NetworkHandler
    private lateinit var leagues: Array<LeagueModel>
    private lateinit var leaguesSpinner: Spinner
    private lateinit var checkChosenLeagueButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        networkHandler = NetworkHandler(this)
        checkChosenLeagueButton = findViewById(R.id.check_league_button)

        /* In professional distribution such kind of array should be fetched,
        * for now it is statically initialized with fixed values.  */
        leagues = arrayOf(
            LeagueModel((-1).toLong(), "Pick a league...", null, null),

            LeagueModel(501.toLong(), "Scottish Premiership",
                "https://cdn.sportmonks.com/images/soccer/leagues/501.png",
                seasons = mapOf(17141 to "2020/2021", 16222 to "2019/2020", 12963 to "2018/2019")),

            LeagueModel(271.toLong(), "Superliga",
                "https://cdn.sportmonks.com/images/soccer/leagues/271.png",
                seasons = mapOf(17328 to "2020/2021", 16020 to "2019/2020", 12919 to "2018/2019"))
        )

        val leaguesAdapter = ArrayAdapter(this,
            R.layout.support_simple_spinner_dropdown_item,
            leagues.map { it.name })

        leaguesSpinner = findViewById(R.id.leagues_spinner)
        leaguesSpinner.onItemSelectedListener = this
        leaguesSpinner.adapter = leaguesAdapter

        checkChosenLeagueButton.setOnClickListener {

            val idx = leaguesSpinner.selectedItemPosition
            val chosenLeague = leagues[idx]
            val chosenLeagueSeasons = chosenLeague.seasons

            /* Necessary requests for possible updates and then running python script with AI */
            //TODO We should put code below into new thread to prevent next activity long loading

            chosenLeagueSeasons?.forEach { (key,_) ->
                networkHandler.sendRequestForRounds(key.toString())
                networkHandler.sendRequestForTeams(chosenLeague.name, key.toString())
                networkHandler.sendRequestForMatches(chosenLeague.id.toString())
            }

            val playedMatchesTable = TemporaryDataHolder.dataBaseHelper.getOnlyPlayedMatches().dropLast(1)
            val nonPlayedMatchesTable = TemporaryDataHolder.dataBaseHelper.getOnlyNonPlayedMatches().dropLast(1)
            val pythonModuleName = "ai_predictioner"
            val pythonFunctionName = "make_predictions"
//            val predictions = getPythonScript(pythonModuleName, pythonFunctionName, playedMatchesTable, nonPlayedMatchesTable).dropLast(1)
            val predictions = """2,2,2
            0,1,1
            2,2,2
            0,1,1
            0,1,0
            0,0,0
            2,0,2
            0,0,0
            2,2,2
            0,1,1
            0,0,0
            0,0,0
            0,2,0
            0,0,0
            0,2,0
            0,0,0
            0,2,0
            0,1,0
            0,1,0
            0,0,0
            0,2,0
            0,0,0
            0,0,0
            0,0,0
            0,0,0
            0,0,0
            2,2,2
            2,1,0
            0,2,0
            2,0,0
            0,0,0
            2,2,2
            2,2,2
            2,2,0
            0,0,0
            0,0,0
            0,0,0
            0,2,2
            2,1,2
            0,0,0
            0,2,0
            0,0,0
            0,0,0
            0,1,0
            2,0,0
            0,0,0
            0,2,0
            2,0,2
            2,2,0
            0,0,0
            2,0,0
            2,2,0
            0,2,0
            2,0,0
            0,0,2
            0,0,0
            0,0,0
            0,1,0
            0,0,0
            0,1,0
            0,0,0
            2,1,0
            2,0,0
            2,0,0
            2,0,0
            0,0,0
            0,2,0
            0,0,0
            0,2,0
            0,2,0
            0,0,0
            2,0,0"""

            val intent  = Intent(this, ChosenLeagueActivity::class.java)

            intent.putExtra("chosenLeagueId", chosenLeague.id)
            intent.putExtra("chosenLeagueName", chosenLeague.name)
            intent.putExtra("chosenLeagueLogoUrl", chosenLeague.logoPath)
            intent.putExtra("nonPlayedMatchesTable", nonPlayedMatchesTable)
            intent.putExtra("predictions", predictions)

            startActivity(intent)
        }
    }


    private fun getPythonScript(module: String, function: String, playedMatches: String, nonPlayedMatches: String) : String {

        val python = Python.getInstance()
        val pythonScript = python.getModule(module)

        return pythonScript.callAttr(function, playedMatches, nonPlayedMatches).toString()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position != 0 )
            checkChosenLeagueButton.visibility = View.VISIBLE
        else
            checkChosenLeagueButton.visibility = View.INVISIBLE
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}