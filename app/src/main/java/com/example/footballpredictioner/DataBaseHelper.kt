package com.example.footballpredictioner

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        const val DATABASE_NAME = "Customers.db"
        const val DATABASE_VERSION = 1

        const val CUSTOMER_TABLE = "CUSTOMER_TABLE"
        const val ID = "ID"
        const val CUSTOMER_NAME = "CUSTOMER_NAME"
        const val CUSTOMER_AGE = "CUSTOMER_AGE"
        const val ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER"
    }


    override fun onCreate(db: SQLiteDatabase?) {

        val createTableQuery = "CREATE TABLE $CUSTOMER_TABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $CUSTOMER_NAME TEXT, $CUSTOMER_AGE INT, $ACTIVE_CUSTOMER BOOL)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun addNewCustomer(customer: CustomerModel):Boolean{

        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(CUSTOMER_NAME,customer.name)
        contentValues.put(CUSTOMER_AGE,customer.age)
        contentValues.put(ACTIVE_CUSTOMER,customer.isActive)

        val insert = db.insert(CUSTOMER_TABLE, null, contentValues)

        return insert != (-1).toLong()

    }


    fun getAllCustomers():List<CustomerModel>{
        val customerList = arrayListOf<CustomerModel>()
        val selectQuery = "SELECT * FROM $CUSTOMER_TABLE"

        val db = this.readableDatabase
        val cursor = db?.rawQuery(selectQuery, null)


        if(cursor?.moveToFirst() == true){
            do {
                val customerID = cursor.getInt(0)
                val customerName = cursor.getString(1)
                val customerAge = cursor.getInt(2)
                val customerIsActive = cursor.getInt(3) == 1

                val newCustomer = CustomerModel(customerID,customerName,customerAge,customerIsActive)
                customerList.add(newCustomer)

            } while (cursor.moveToNext())

        }

        cursor?.close()
        db.close()
        return customerList
    }

}