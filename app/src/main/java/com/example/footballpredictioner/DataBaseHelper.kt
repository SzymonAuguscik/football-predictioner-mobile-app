package com.example.footballpredictioner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import org.json.JSONObject

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "ProfessionalFootball.db"

        const val TEAMS_TABLE = "TEAMS"
        const val TEAM_ID = "ID"
        const val TEAM_NAME = "TEAM_NAME"
        const val LOGO_URL = "LOGO_URL"
        const val STADIUM = "STADIUM"
        const val WINS = "WINS"
        const val LOSES = "LOSES"
        const val DRAWS = "DRAWS"
        const val SEASON = "SEASON"
        const val POINTS = "POINTS"


        const val ROUNDS_TABLE = "ROUNDS_TABLE"
        const val ROUND_ID = "ID"
        const val RD_NUMBER = "RD_NUMBER"
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"


        const val MATCHES_TABLE = "MATCHES_TABLE"
        const val MATCH_ID = "ID"
        //const val RD_NUMBER = "RD_NUMBER"
        const val LOCAL_TEAM_ID = "LOCAL_TEAM_ID"
        const val VISITOR_TEAM_ID = "VISITOR_TEAM_ID"
        const val LOCAL_TEAM_SCORE = "LOCAL_TEAM_SCORE"
        const val VISITOR_TEAM_SCORE = "VISITOR_TEAM_SCORE"
        const val DATE = "DATE"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTeamsTableQuery = "CREATE TABLE IF NOT EXISTS $TEAMS_TABLE ($TEAM_ID INTEGER PRIMARY KEY, $TEAM_NAME TEXT, $LOGO_URL TEXT, $STADIUM TEXT, $WINS INTEGER, $LOSES INTEGER, $DRAWS INTEGER, $SEASON TEXT, $POINTS INTEGER)"
        val createRoundsTableQuery = "CREATE TABLE IF NOT EXISTS $ROUNDS_TABLE ($ROUND_ID INTEGER PRIMARY KEY, $RD_NUMBER INTEGER, $START_DATE TEXT, $END_DATE TEXT)"
        val createMatchesTableQuery = "CREATE TABLE IF NOT EXISTS $MATCHES_TABLE ($MATCH_ID INTEGER PRIMARY KEY, $RD_NUMBER INTEGER, $LOCAL_TEAM_ID INTEGER, $VISITOR_TEAM_ID INTEGER, $LOCAL_TEAM_SCORE INTEGER, $VISITOR_TEAM_SCORE INTEGER, $DATE TEXT)"

        db?.execSQL(createTeamsTableQuery)
        db?.execSQL(createRoundsTableQuery)
        db?.execSQL(createMatchesTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addTeams(response: JSONObject, leagueName: String){
        response.let {
            val teams = response.getJSONArray("data")
            val teamsCount = teams.length()
            println(teamsCount)
//            for (i in 0 until teamsCount){
//                val team = teams.get(i) as JSONObject
//                val teamId = team.getInt("id")
//                val teamName = team.getInt("name")
//                val teamLogoPath = team.getInt("logo_path")
//                val teamVenueId = team.getInt("venue_id")
//                val seasonId = team.getInt("current_season_id")
//
//
//
//            }
        }
    }



//    fun addNewCustomer(customer: CustomerModel):Boolean{
//
//        val db = this.writableDatabase
//
//        val contentValues = ContentValues()
//        contentValues.put(CUSTOMER_NAME,customer.name)
//        contentValues.put(CUSTOMER_AGE,customer.age)
//        contentValues.put(ACTIVE_CUSTOMER,customer.isActive)
//
//        val insert = db.insert(CUSTOMER_TABLE, null, contentValues)
//
//        return insert != (-1).toLong()
//
//    }
//
//
//    fun getAllCustomers():List<CustomerModel>{
//        val customerList = arrayListOf<CustomerModel>()
//        val selectQuery = "SELECT * FROM $CUSTOMER_TABLE"
//
//        val db = this.readableDatabase
//        val cursor = db?.rawQuery(selectQuery, null)
//
//
//        if(cursor?.moveToFirst() == true){
//            do {
//                val customerID = cursor.getInt(0)
//                val customerName = cursor.getString(1)
//                val customerAge = cursor.getInt(2)
//                val customerIsActive = cursor.getInt(3) == 1
//
//                val newCustomer = CustomerModel(customerID,customerName,customerAge,customerIsActive)
//                customerList.add(newCustomer)
//
//            } while (cursor.moveToNext())
//
//        }
//
//        cursor?.close()
//        db.close()
//        return customerList
//    }

}