package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddActivity : AppCompatActivity() {
    private lateinit var bookTitle: EditText
    private lateinit var bookAuthor: EditText
    private lateinit var bookLexile: EditText
    private lateinit var bookAge: EditText
    private lateinit var bookCategory: Spinner
    private lateinit var bookCopies: EditText
    private lateinit var scanBtn: Button
    private lateinit var addBtn: Button
    var userEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        userEmail = intent.getStringExtra("username").toString()
        //Log.d("ClassLib", userEmail)
        initializeUI()
        addBtn.setOnClickListener { addBook() }

    }

    private fun addBook(){
        val data:HashMap<String,String> = HashMap<String,String>()
        data.put("Title", bookTitle.text.toString() ?: "")
        data.put("Author", bookAuthor.text.toString() ?: "")
        data.put("Lexile Level", bookLexile.text.toString() ?: "")
        data.put("Age", bookAge.text.toString() ?: "")
        data.put("Checked Out", "")

        // not been implemented yet
        //data.put("Category", bookCategory.selectedItem.toString())
        data.put("Number of Copies", bookCopies.text.toString() ?: "")


        // will require the title to be entered!
        if(bookTitle.text.toString() != "") {
            val db = Firebase.firestore
            // will merge (update) the field, if there is already a book in the database, if not,
            // it will create it
            db.collection(userEmail).document(bookTitle.text.toString())
                .set(data, SetOptions.merge())
            finish()
        }
        else{
            Toast.makeText(applicationContext, "Please enter the Title", Toast.LENGTH_LONG).show()
            return
        }
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