package com.example.classlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class ReportActivity : AppCompatActivity() {
    private lateinit var getLibraryButton: Button
    private lateinit var getCheckedOutButton: Button
    private lateinit var sortCategory: Spinner
    private lateinit var sortCategory2: Spinner
    private lateinit var sorting: Array<String>
    private lateinit var sorting2: Array<String>
    var userEmail = ""
    val TAG = "ClassLib"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        userEmail = intent.getStringExtra("username").toString()
        sorting = resources.getStringArray(R.array.Sorting)
        sorting2 = resources.getStringArray(R.array.Sorting2)
        initializeUI()
        getLibraryButton.setOnClickListener { getLibrary() }
        getCheckedOutButton.setOnClickListener { getCheckedOut() }
    }

    // gets the report for the whole library for this user
    fun getLibrary(){
        intent = Intent(this, ReportResultActivity::class.java)
        intent.putExtra("username", userEmail)
        intent.putExtra("sort", sortCategory.selectedItem.toString())
        intent.putExtra("sort2", sortCategory2.selectedItem.toString())
        if(sortCategory.selectedItem.toString() != sortCategory2.selectedItem.toString()) {
            startActivity(intent)
        }
        else{
            Toast.makeText(applicationContext, "Can't sort by the same category!",
                Toast.LENGTH_LONG).show()
        }
    }

    // gets the report for the books that have been checked out
    fun getCheckedOut(){
        intent = Intent(this, ReportResultActivity::class.java)
        intent.putExtra("username", userEmail)
        intent.putExtra("sort", sortCategory.selectedItem.toString())
        intent.putExtra("sort2", sortCategory2.selectedItem.toString())
        intent.putExtra("get", "checked")
        if(sortCategory.selectedItem.toString() != sortCategory2.selectedItem.toString()) {
            startActivity(intent)
        }
        else{
            Toast.makeText(applicationContext, "Can't sort by the same category!",
                Toast.LENGTH_LONG).show()
        }
    }


    private fun initializeUI() {
        getLibraryButton = findViewById(R.id.getLibraryButton)
        getCheckedOutButton = findViewById(R.id.getCheckedOutButton)
        sortCategory = findViewById(R.id.sort_spinner)
        sortCategory2 = findViewById(R.id.sort_spinner2)
        if (sortCategory!= null){
            val adapter = ArrayAdapter(this,
                R.layout.support_simple_spinner_dropdown_item, sorting)
            sortCategory.adapter = adapter
        }

        if (sortCategory2!= null){
            val adapter =
                ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sorting2)
            sortCategory2.adapter = adapter
        }
    }

}