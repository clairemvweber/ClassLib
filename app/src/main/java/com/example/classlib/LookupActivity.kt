package com.example.classlib

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.util.Log
import android.widget.Toast

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
        lookupBtn.setOnClickListener { fetchBook() }
        scanBtn.setOnClickListener { scanBarcode() }
    }

    private fun fetchBook() {
        val intent = Intent(
            this@LookupActivity,
            NetworkingJSONResponseActivity::class.java
        )
        Log.i("Fetch:", bookISBN.text.toString())
        intent.putExtra("isbn", bookISBN.text.toString())
        startActivityForResult(intent, 2)
    }

    private fun scanBarcode() {
        val intent = Intent(this@LookupActivity, ScannerActivity::class.java)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(
            "ISBN ACTIVITY",
            "request: " + requestCode.toString() + "result:" + resultCode.toString()
        )
        if (requestCode == BARCODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    bookISBN.setText(data.getStringExtra("ISBN"))
                    fetchBook()
                }
            }
        } else if (requestCode == LOOKUP_REQUEST && resultCode == RESULT_OK) {

            setResult(resultCode, data)
            finish()
        } else if (requestCode == LOOKUP_REQUEST && resultCode == RESULT_NOT_FOUND) {
            Log.i("Lookup", "NO RESULT FROM LOOKUP")
            Toast.makeText(
                this,
                "ISBN not in database. Please enter book details manually.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            setResult(resultCode, data)
            finish()
        }

    }

    companion object {
        const val BARCODE_REQUEST = 1
        const val LOOKUP_REQUEST = 2
        const val RESULT_NOT_FOUND = 3
    }
}