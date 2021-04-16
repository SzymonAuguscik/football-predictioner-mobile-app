package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.chaquo.python.Python
import com.example.footballpredictioner.api.NetworkHandler
import com.example.footballpredictioner.api.TemporaryDataHolder


class MainActivity : AppCompatActivity() {


    private lateinit var fetchDataButton: Button
    private lateinit var networkHandler: NetworkHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchDataButton = findViewById(R.id.fetch_btn)
        networkHandler = NetworkHandler(this)


        fetchDataButton.setOnClickListener{

            // Premiership last seasons id
            // 17141 - 2020/2021
            // 16222 - 2019/2020
            // 12963 - 2018/2019

            // Superliga last seasons id
            // 17328 - 2020/2021
            // 16020 - 2019/2020
            // 12919 - 2018/2019


//            networkHandler.sendRequestForRounds("17141")
//            networkHandler.sendRequestForTeams("Scottish Premiership","17141")
//
//            networkHandler.sendRequestForRounds("16222")
//            networkHandler.sendRequestForTeams("Scottish Premiership","16222")
//
//            networkHandler.sendRequestForRounds("12963")
//            networkHandler.sendRequestForTeams("Scottish Premiership","12963")
//
//
//
//            networkHandler.sendRequestForRounds("17328")
//            networkHandler.sendRequestForTeams("Superliga","17328")
//
//            networkHandler.sendRequestForRounds("16020")
//            networkHandler.sendRequestForTeams("Superliga","16020")
//
//            networkHandler.sendRequestForRounds("12919")
//            networkHandler.sendRequestForTeams("Superliga","12919")
//
//
//            networkHandler.sendRequestForMatches(listOf("501","271"))
            
            val playedMatchesTable = TemporaryDataHolder.dataBaseHelper.getOnlyPlayedMatches().dropLast(1)
            val nonPlayedMatchesTable = TemporaryDataHolder.dataBaseHelper.getOnlyNonPlayedMatches().dropLast(1)
            val pythonModuleName = "ai_predictioner"
            val pythonFunctionName = "make_predictions"
            val pythonResult = getPythonScript(pythonModuleName, pythonFunctionName, playedMatchesTable, nonPlayedMatchesTable)
            println(pythonResult)

            Toast.makeText(this, "Send API request", Toast.LENGTH_SHORT).show()
        }

    }

    private fun getPythonScript(module: String, function: String, playedMatches: String, nonPlayedMatches: String) : String {

        val python = Python.getInstance()
        val pythonScript = python.getModule(module)

        return pythonScript.callAttr(function, playedMatches, nonPlayedMatches).toString()
    }

}