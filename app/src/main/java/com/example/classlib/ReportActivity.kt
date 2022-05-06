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
    private lateinit var sorting: Array<String>
    var userEmail = ""
    val TAG = "ClassLib"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        userEmail = intent.getStringExtra("username").toString()
        sorting = resources.getStringArray(R.array.Sorting)
        initializeUI()
        getLibraryButton.setOnClickListener { getLibrary() }
        getCheckedOutButton.setOnClickListener { getCheckedOut() }
    }

    fun getLibrary(){
        intent = Intent(this, ReportResultActivity::class.java)
        intent.putExtra("username", userEmail)
        intent.putExtra("sort", sortCategory.selectedItem.toString())
        intent.putExtra("get", "all")
        startActivity(intent)
    }

    fun getCheckedOut(){
        intent = Intent(this, ReportResultActivity::class.java)
        intent.putExtra("username", userEmail)
        intent.putExtra("sort", sortCategory.selectedItem.toString())
        intent.putExtra("get", "checked")
        startActivity(intent)
    }


    private fun initializeUI() {
        getLibraryButton = findViewById(R.id.getLibraryButton)
        getCheckedOutButton = findViewById(R.id.getCheckedOutButton)
        sortCategory = findViewById(R.id.sort_spinner)
        if (sortCategory!= null){
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, sorting)
            sortCategory.adapter=adapter
        }
    }

}