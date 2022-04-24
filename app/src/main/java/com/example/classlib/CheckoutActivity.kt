package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner

class CheckoutActivity : AppCompatActivity() {
    private lateinit var bookTitle: EditText
    private lateinit var studentID: EditText
    private lateinit var barcodeButton: Button
    private lateinit var returnButton: Button
    private lateinit var checkoutButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        initializeUI()

    }
    private fun initializeUI() {
        bookTitle = findViewById(R.id.book_title_return)
        studentID = findViewById(R.id.student_id_return)
        barcodeButton = findViewById(R.id.scan_barcode_return)
        checkoutButton = findViewById(R.id.checkout_book_Button)
        returnButton = findViewById(R.id.return_book)
    }

}