package com.example.footballpredictioner.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.footballpredictioner.models.MatchModel
import com.example.footballpredictioner.models.RoundModel
import com.example.footballpredictioner.models.TeamModel

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Football.db"

        const val TEAMS_TABLE = "TEAMS"
        const val TEAM_ID = "TEAM_ID"
        const val TEAM_NAME = "TEAM_NAME"
        const val LOGO_URL = "LOGO_URL"
        const val STADIUM_ID = "STADIUM"
        const val WINS = "WINS"
        const val LOSES = "LOSES"
        const val DRAWS = "DRAWS"
        const val SEASON_ID = "SEASON_ID"


        const val ROUNDS_TABLE = "ROUNDS_TABLE"
        const val ROUND_ID = "ROUND_ID"
        const val NUMBER = "NUMBER"
        const val LEAGUE_ID = "LEAGUE_ID"
//        const val SEASON_ID = "SEASON"
        const val START_DATE = "START_DATE"
        const val END_DATE = "END_DATE"


        const val MATCHES_TABLE = "MATCHES_TABLE"
        const val MATCH_ID = "MATCH_ID"
//        const val ROUND_ID = "ROUND_ID"
        const val LOCAL_TEAM_ID = "LOCAL_TEAM_ID"
        const val VISITOR_TEAM_ID = "VISITOR_TEAM_ID"
        const val LOCAL_TEAM_SCORE = "LOCAL_TEAM_SCORE"
        const val VISITOR_TEAM_SCORE = "VISITOR_TEAM_SCORE"
        const val DATE = "DATE"

    }

    override fun onCreate(db: SQLiteDatabase?) {

        val createTeamsTableQuery = "CREATE TABLE IF NOT EXISTS $TEAMS_TABLE " +
                "($TEAM_ID INTEGER , $TEAM_NAME TEXT, $LOGO_URL TEXT, " +
                "$STADIUM_ID TEXT, $WINS INTEGER, $LOSES INTEGER, $DRAWS INTEGER, " +
                "$SEASON_ID TEXT," +
                "PRIMARY KEY ($TEAM_ID, $SEASON_ID) )"

        val createRoundsTableQuery = "CREATE TABLE IF NOT EXISTS $ROUNDS_TABLE " +
                "($ROUND_ID INTEGER PRIMARY KEY, $NUMBER INTEGER, $LEAGUE_ID INTEGER, " +
                "$SEASON_ID INTEGER, $START_DATE TEXT, $END_DATE TEXT)"


        val createMatchesTableQuery = "CREATE TABLE IF NOT EXISTS $MATCHES_TABLE " +
                "($MATCH_ID INTEGER PRIMARY KEY, $ROUND_ID INTEGER, $LOCAL_TEAM_ID INTEGER, " +
                "$VISITOR_TEAM_ID INTEGER, $LOCAL_TEAM_SCORE INTEGER, $VISITOR_TEAM_SCORE INTEGER, $DATE TEXT, " +
                "FOREIGN KEY($ROUND_ID) REFERENCES $ROUNDS_TABLE($ROUND_ID)," +
                "FOREIGN KEY($LOCAL_TEAM_ID) REFERENCES $TEAMS_TABLE($TEAM_ID), " +
                "FOREIGN KEY($VISITOR_TEAM_ID) REFERENCES $TEAMS_TABLE($TEAM_ID) )"


        db?.execSQL(createTeamsTableQuery)
        db?.execSQL(createRoundsTableQuery)
        db?.execSQL(createMatchesTableQuery)
    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TEAMS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $ROUNDS_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $MATCHES_TABLE")
        onCreate(db)
    }



    fun addNewTeam(team: TeamModel):Boolean{
        val db = this.writableDatabase

        if(!checkIfTeamRecordChanged(team.name, team.wins+team.draws+team.loses)){
            val contentValues = ContentValues()
            contentValues.put(TEAM_ID, team.id)
            contentValues.put(TEAM_NAME, team.name)
            contentValues.put(LOGO_URL, team.logoPath)
            contentValues.put(STADIUM_ID, team.venueName)
            contentValues.put(WINS, team.wins)
            contentValues.put(LOSES, team.loses)
            contentValues.put(DRAWS, team.draws)
            contentValues.put(SEASON_ID, team.season)



            val insert = db.insert(TEAMS_TABLE, null, contentValues)
            db.close()

            return insert != (-1).toLong()
        }

        return false

    }

    private fun checkIfTeamRecordChanged(teamName:String, sumOfMatches:Int):Boolean{

        val checkQuery = "SELECT * FROM $TEAMS_TABLE WHERE $TEAM_NAME = '$teamName'"
        val db = this.readableDatabase

        val cursor = db.rawQuery(checkQuery, null)

        var sum = 0
        if (cursor != null && cursor.count > 0 ) {
            cursor.moveToFirst()
            do{
                val wins = cursor.getInt(cursor.getColumnIndex(WINS))
                val draws = cursor.getInt(cursor.getColumnIndex(DRAWS))
                val loses = cursor.getInt(cursor.getColumnIndex(LOSES))
                sum = wins+draws+loses
            } while(cursor.moveToNext())
        }

        cursor.close()

        return sum == sumOfMatches
    }


    fun addNewRound(round: RoundModel):Boolean{
        val db = this.writableDatabase


        if(!checkIfRoundExistsInDb(round.id)){
            val contentValues = ContentValues()

            contentValues.put(ROUND_ID, round.id)
            contentValues.put(LEAGUE_ID, round.leagueId)
            contentValues.put(SEASON_ID, round.seasonId)
            contentValues.put(NUMBER, round.num)
            contentValues.put(START_DATE, round.startDate)
            contentValues.put(END_DATE, round.endDate)


            val insert = db.insert(ROUNDS_TABLE, null, contentValues)
            db.close()

            return insert != (-1).toLong()
        }

        return false

    }


    @SuppressLint("Recycle")
    private fun checkIfRoundExistsInDb(id:Long): Boolean {

        val checkQuery = "SELECT * FROM $ROUNDS_TABLE WHERE $ROUND_ID = '$id'"
        val db = this.readableDatabase
        val cursor = db.rawQuery(checkQuery, null)

        return (cursor != null && cursor.count >0)
    }



    @SuppressLint("Recycle")
    fun getSeasonBorderDates(): Pair<String, String>? {

        val queryForStartDate = "SELECT $START_DATE FROM $ROUNDS_TABLE ORDER BY $START_DATE ASC LIMIT 1"
        val queryForEndDate = "SELECT $END_DATE FROM $ROUNDS_TABLE ORDER BY $END_DATE DESC LIMIT 1"

        val db = this.readableDatabase

        val cursorForStartDate = db.rawQuery(queryForStartDate, null)
        val cursorForEndDate = db.rawQuery(queryForEndDate, null)

        var result: Pair<String,String>? = null

        if (cursorForStartDate != null && cursorForStartDate.count > 0  && cursorForEndDate != null && cursorForEndDate.count > 0 ) {
            cursorForStartDate.moveToFirst()
            cursorForEndDate.moveToFirst()

            val startDate = cursorForStartDate.getString(0)
            val endDate = cursorForEndDate.getString(0)
            result = Pair(startDate,endDate)

        }

        cursorForStartDate.close()
        cursorForEndDate.close()
        db.close()

        return result
    }


    fun addNewMatch(match: MatchModel):Boolean{

        val db = this.writableDatabase

        if(!checkIfMatchExists(match.id)){
            val contentValues = ContentValues()

            contentValues.put(MATCH_ID, match.id)
            contentValues.put(ROUND_ID, match.roundId)
            contentValues.put(LOCAL_TEAM_ID, match.localTeamId)
            contentValues.put(VISITOR_TEAM_ID, match.visitorTeamId)
            contentValues.put(LOCAL_TEAM_SCORE, match.localTeamScore)
            contentValues.put(VISITOR_TEAM_SCORE, match.visitorTeamScore)
            contentValues.put(DATE, match.date)

            val insert = db.insert(MATCHES_TABLE, null, contentValues)
            db.close()

            return insert != (-1).toLong()
        }

        return false

    }

    @SuppressLint("Recycle")
    private fun checkIfMatchExists(id:Long): Boolean {
        val checkQuery = "SELECT * FROM $MATCHES_TABLE WHERE $MATCH_ID= '$id'"
        val db = this.readableDatabase

        val cursor = db.rawQuery(checkQuery, null)

        return (cursor != null && cursor.count > 0)
    }

    @SuppressLint("Recycle")
    fun getSelectAsText(query: String) : String {
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        var result = ""

        if (cursor != null && cursor.count > 0 ) {
            while(cursor.moveToNext()) {
                for(i in 0 until cursor.columnCount) {
                    result += cursor.getString(i)
                    result += if (i != cursor.columnCount - 1) "," else "\n"
                }
            }
        }

        db.close()

        return result
    }

    fun getOnlyPlayedMatches() : String {
        val query = "SELECT $LOCAL_TEAM_ID, $VISITOR_TEAM_ID, $LOCAL_TEAM_SCORE, $VISITOR_TEAM_SCORE " +
                "FROM $MATCHES_TABLE WHERE DATE < (SELECT DATE('now'))"

        return getSelectAsText(query)
    }

    fun getOnlyNonPlayedMatches() : String {
        val query = "SELECT $LOCAL_TEAM_ID, $VISITOR_TEAM_ID " +
                "FROM $MATCHES_TABLE WHERE DATE > (SELECT DATE('now'))"

        return getSelectAsText(query)
    }

    //TODO: refactor!!!
    fun getTeamsFromSeason(seasonID: String?) : String {
        val query = "SELECT $TEAM_ID, $TEAM_NAME FROM $TEAMS_TABLE WHERE $SEASON_ID = $seasonID"

        return getSelectAsText(query)
    }

    fun getDateOfMatch(firstTeam: String, secondTeam: String) : String {
        val query = "SELECT $DATE FROM $MATCHES_TABLE WHERE $LOCAL_TEAM_ID = $firstTeam AND " +
                    "$VISITOR_TEAM_ID = $secondTeam AND $DATE > DATE('now')"

        return getSelectAsText(query)
    }

    @SuppressLint("Recycle")
    fun getTeamsFromGivenSeason(seasonId:Int, league:String):ArrayList<TeamModel>{

        val resultArray = arrayListOf<TeamModel>()

        val query = "SELECT * FROM $TEAMS_TABLE WHERE $SEASON_ID = $seasonId"

        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        if (cursor != null && cursor.count > 0 ) {
            while(cursor.moveToNext()) {
                val teamId = cursor.getLong(cursor.getColumnIndex(TEAM_ID))
                val name = cursor.getString(cursor.getColumnIndex(TEAM_NAME))
                val logoPath = cursor.getString(cursor.getColumnIndex(LOGO_URL))
                val season = cursor.getString(cursor.getColumnIndex(SEASON_ID))
                val wins = cursor.getInt(cursor.getColumnIndex(WINS))
                val loses = cursor.getInt(cursor.getColumnIndex(LOSES))
                val draws = cursor.getInt(cursor.getColumnIndex(DRAWS))
                val team = TeamModel(teamId, name, logoPath, league, season = season, wins = wins, loses = loses, draws = draws)
                resultArray.add(team)
            }
        }

        db.close()

        return resultArray




    }
}