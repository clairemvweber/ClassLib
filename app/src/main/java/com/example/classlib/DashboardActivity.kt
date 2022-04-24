package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class DashboardActivity : AppCompatActivity() {
    private lateinit var searchBtn: Button
    private lateinit var addBtn: Button
    private lateinit var checkoutBtn: Button
    private lateinit var reportsBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        initializeUI()
        addBtn.setOnClickListener { addBook() }
    }
    private fun addBook(){
        startActivity(Intent(this@DashboardActivity, AddActivity::class.java))
    }
    private fun initializeUI() {
        searchBtn = findViewById(R.id.searchLibraryButton)
        addBtn = findViewById(R.id.addBookButton)
        checkoutBtn = findViewById(R.id.checkoutButton)
        reportsBtn = findViewById(R.id.reportsButton)
    }
}
