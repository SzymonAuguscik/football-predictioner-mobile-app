package com.example.footballpredictioner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.chaquo.python.Python
import java.lang.Integer.parseInt


class MainActivity : AppCompatActivity() {

    private lateinit var nameTextView: EditText
    private lateinit var ageTextView: EditText
    private lateinit var viewAllButton: Button
    private lateinit var addButton: Button
    private lateinit var activeCustomerSwitch: Switch
    private lateinit var listOfCustomers: ListView

    private val testList = listOf("Tom","John","Martha","Peter")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewAllButton = findViewById(R.id.btn_view_all)
        addButton = findViewById(R.id.btn_add)
        activeCustomerSwitch = findViewById(R.id.switch1)
        listOfCustomers = findViewById(R.id.custmer_list)
        nameTextView = findViewById(R.id.et_name)
        ageTextView = findViewById(R.id.et_age)

        val textFromPython = getPythonScript()

        addButton.setOnClickListener{

            val customerModel = CustomerModel(-1, nameTextView.text.toString(), parseInt(ageTextView.text.toString()), activeCustomerSwitch.isChecked)
            val dataBaseHelper = DataBaseHelper(applicationContext)
            val success = dataBaseHelper.addNewCustomer(customerModel)

            Toast.makeText(this, success.toString(), Toast.LENGTH_SHORT).show()

        }

        viewAllButton.setOnClickListener{

            val dataBaseHelper = DataBaseHelper(applicationContext)
            val allCustomers = dataBaseHelper.getAllCustomers()


            Toast.makeText(this, textFromPython, Toast.LENGTH_SHORT).show()
        }



    }


    private fun getPythonScript():String{
        val python = Python.getInstance()
        val pythonScript = python.getModule("test")
        return pythonScript.callAttr("hello", testList).toString()
    }

}