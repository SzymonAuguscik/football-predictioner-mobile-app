package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.chaquo.python.Python
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {


    private lateinit var fetchDataButton: Button
    private lateinit var networkHandler:NetworkHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchDataButton = findViewById(R.id.fetch_btn)
        networkHandler = NetworkHandler(this)


        fetchDataButton.setOnClickListener{

            networkHandler.sendRequestForLeagueTeams("Premier")
            Toast.makeText(this, "Send API request", Toast.LENGTH_SHORT).show()

        }


    }



    private fun getPythonScript():String{
        val python = Python.getInstance()
        val pythonScript = python.getModule("test")
        return pythonScript.callAttr("hello", "testList.toList()").toString()
    }

}