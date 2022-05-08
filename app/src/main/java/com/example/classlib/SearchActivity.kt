package com.example.classlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.android.material.slider.RangeSlider

class SearchActivity : AppCompatActivity() {
    private lateinit var user: String
    private lateinit var title: EditText
    private lateinit var author: EditText
    private lateinit var category: Spinner
    private lateinit var lexileLevel: RangeSlider
    private lateinit var age: RangeSlider
    private lateinit var available: CheckBox
    private lateinit var searchButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initializeUI()
    }

    private fun initializeUI() {
        user = intent.getStringExtra("username").toString()
        title = findViewById(R.id.search_title_input)
        author = findViewById(R.id.search_author_input)

        category = findViewById(R.id.search_category_spinner)

        val adapter = ArrayAdapter(
            this, R.layout.support_simple_spinner_dropdown_item,
            resources.getStringArray(R.array.categories)
        )
        category.adapter = adapter

        lexileLevel = findViewById(R.id.search_lexile_range)
        age = findViewById(R.id.search_age_range)
        available = findViewById(R.id.search_availability)
        searchButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener { search() }
    }

    // Stores input as intent extras and starts SearchResultsActivity
    private fun search() {
        val availableInput = if (available.isChecked) "1" else "0"

        val queryArray = arrayOf(
            title.text.toString(),
            author.text.toString(),
            category.selectedItem.toString(),
            lexileLevel.values[0].toInt().toString(), lexileLevel.values[1].toInt().toString(),
            age.values[0].toInt().toString(), age.values[1].toInt().toString(),
            availableInput
        )

        val searchIntent = Intent(this, SearchResultsActivity::class.java)

        searchIntent.putExtra("user", user)
        searchIntent.putExtra("queryArray", queryArray)

        startActivity(searchIntent)
    }
}
