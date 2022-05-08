package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DashboardActivity : AppCompatActivity() {
    private lateinit var searchBtn: Button
    private lateinit var addBtn: Button
    private lateinit var checkoutBtn: Button
    private lateinit var reportsBtn: Button
    var userEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        val TAG = "ClassLib"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initializeUI()
        // get the logged in user's email address
        userEmail = intent.getStringExtra("username").toString()
        addBtn.setOnClickListener { addBook() }
        checkoutBtn.setOnClickListener { checkout()}
        searchBtn.setOnClickListener { searchLibrary() }
        reportsBtn.setOnClickListener { report() }

    }

    // start addBook activity
    private fun addBook(){
        intent = Intent(this@DashboardActivity, AddActivity::class.java)
        intent.putExtra("username", userEmail)
        startActivity(intent)
    }

    // start checkOut activity
    private fun checkout(){
        intent = Intent(this@DashboardActivity, CheckoutActivity::class.java)
        intent.putExtra("username", userEmail)
        startActivity(intent)
    }

    // start searchLibrary activity
    private fun searchLibrary(){
        intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("username", userEmail)
        startActivity(intent)
    }

    // start report activity
    private fun report(){
        intent = Intent(this@DashboardActivity, ReportActivity::class.java)
        intent.putExtra("username", userEmail)
        startActivity(intent)
    }

    private fun initializeUI() {
        searchBtn = findViewById(R.id.searchLibraryButton)
        addBtn = findViewById(R.id.addBookButton)
        checkoutBtn = findViewById(R.id.checkoutButton)
        reportsBtn = findViewById(R.id.reportsButton)
    }
}
