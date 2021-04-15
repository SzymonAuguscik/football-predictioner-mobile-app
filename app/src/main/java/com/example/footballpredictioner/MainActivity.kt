package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.chaquo.python.Python
import java.lang.Integer.parseInt


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
//
//            networkHandler.sendRequestForRounds("17328")
//            networkHandler.sendRequestForTeams("Superliga","17328")

//            networkHandler.sendRequestForMatches()

            Toast.makeText(this, "Send API request", Toast.LENGTH_SHORT).show()

        }


    }


    private fun getPythonScript():String{
        val python = Python.getInstance()
        val pythonScript = python.getModule("test")
        return pythonScript.callAttr("hello", "testList.toList()").toString()
    }

}