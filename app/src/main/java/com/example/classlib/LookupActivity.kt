package com.example.classlib

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log

class LookupActivity : AppCompatActivity() {
    private lateinit var bookISBN: EditText
    private lateinit var scanBtn: Button
    private lateinit var lookupBtn: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lookup)
        bookISBN = findViewById(R.id.book_isbn)
        scanBtn = findViewById(R.id.scan_button)
        lookupBtn = findViewById(R.id.lookup_button)
        lookupBtn.setOnClickListener{fetchBook()}
    }
    private fun fetchBook() {
        val intent = Intent(
            this@LookupActivity,
            NetworkingJSONResponseActivity::class.java
        )
        intent.putExtra("isbn", bookISBN.text.toString())
        startActivityForResult(intent, 0)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        setResult(resultCode,data)
        finish()

    }
}