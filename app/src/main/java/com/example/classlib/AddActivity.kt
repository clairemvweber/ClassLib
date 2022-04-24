package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class AddActivity : AppCompatActivity() {
    private lateinit var bookTitle: EditText
    private lateinit var bookAuthor: EditText
    private lateinit var bookLexile: EditText
    private lateinit var bookAge: EditText
    private lateinit var bookCategory: Spinner
    private lateinit var bookCopies: EditText
    private lateinit var scanBtn: Button
    private lateinit var addBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        initializeUI()

    }
    private fun initializeUI() {
        bookTitle = findViewById(R.id.book_title)
        bookAuthor = findViewById(R.id.book_author)
        bookLexile = findViewById(R.id.book_lexile)
        bookAge = findViewById(R.id.book_age)
        bookCategory = findViewById(R.id.category_spinner)
        bookCopies = findViewById(R.id.book_copies)
        scanBtn = findViewById(R.id.scan_button)
        addBtn = findViewById(R.id.add_button)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }
}