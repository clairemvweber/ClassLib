package com.example.classlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Spinner
import com.google.android.material.slider.RangeSlider
import android.widget.CheckBox
import android.widget.Button

class SearchActivity : AppCompatActivity() {
    private lateinit var title: EditText
    private lateinit var author: EditText
    private lateinit var genre: Spinner
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
        title = findViewById(R.id.search_title_input)
        author = findViewById(R.id.search_author_input)
        genre = findViewById(R.id.search_genre_spinner)
        lexileLevel = findViewById(R.id.search_lexile_range)
        age = findViewById(R.id.search_age_range)
        available = findViewById(R.id.search_availability)
        searchButton = findViewById(R.id.search_button)
        searchButton.setOnClickListener{ search() }
    }

    private fun search() {
        val titleInput = title.text.toString()
        val authorInput = author.text.toString()
        val genreInput = genre.selectedItem.toString()
        val lexileMinInput = lexileLevel.valueFrom.toString()
        val lexileMaxInput = lexileLevel.valueTo.toString()
        val ageMinInput = age.valueFrom.toString()
        val ageMaxInput = age.valueTo.toString()
        val availableInput = available.isChecked.toString()

        val queryArray = arrayOf(titleInput, authorInput, genreInput,
            lexileMinInput, lexileMaxInput, ageMinInput, ageMaxInput, availableInput)

        val searchIntent = Intent()

        searchIntent.putExtra("queryArray", queryArray)

        startActivity(searchIntent)
    }
}