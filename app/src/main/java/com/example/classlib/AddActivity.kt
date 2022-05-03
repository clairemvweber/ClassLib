package com.example.classlib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.*
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
    private lateinit var categories: Array<String>
    var userEmail = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)
        userEmail = intent.getStringExtra("username").toString()
        categories = resources.getStringArray(R.array.Categories)
        initializeUI()
        addBtn.setOnClickListener { addBook() }
        scanBtn.setOnClickListener{ isbnLookup()}

    }

    private fun addBook(){
        val data:HashMap<String,String> = HashMap<String,String>()
        data.put("Title", bookTitle.text.toString() ?: "")
        data.put("Author", bookAuthor.text.toString() ?: "")
        data.put("Lexile Level", bookLexile.text.toString() ?: "")
        data.put("Age", bookAge.text.toString() ?: "")
        data.put("Checked Out", "")
        data.put("Category", bookCategory.selectedItem.toString())
        data.put("Number of Copies", bookCopies.text.toString() ?: "")


        // will require the title to be entered!
        if(bookTitle.text.toString() != "") {
            val db = Firebase.firestore
            // will merge (update) the field, if there is already a book in the database, if not,
            // it will create it
            db.collection(userEmail).document(bookTitle.text.toString().uppercase())
                .set(data, SetOptions.merge())
            Toast.makeText(applicationContext, "Sucessfully added the book!", Toast.LENGTH_LONG).show()
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
        if (bookCategory!= null){
            val adapter = ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, categories)
            bookCategory.adapter=adapter
        }
        bookCopies = findViewById(R.id.book_copies)
        scanBtn = findViewById(R.id.search_isbn_button)
        addBtn = findViewById(R.id.add_button)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.top_menu, menu)
        return true
    }
    private fun isbnLookup() {

        startActivityForResult(
            Intent(
                this@AddActivity,
                LookupActivity::class.java
            ),0
        )
    }
    private fun clear(){
        bookTitle.setText(null)
        bookAuthor.setText(null)
        bookLexile.setText(null)
        bookCategory.setSelection(0)
        bookAge.setText(null)
        bookCopies.setText(null)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            clear()
            bookTitle.setText(data.getStringExtra(TITLE_TAG))
            bookAuthor.setText(data.getStringExtra(AUTHORS_TAG))
            bookLexile.setText(data.getStringExtra(LEXILE_TAG))
            bookCategory.setSelection(categories.indexOf(data.getStringExtra(CATEGORIES_TAG)))
            if (data.getStringExtra(MIN_AGE_TAG) != "null"){
                bookAge.setText(data.getStringExtra(MIN_AGE_TAG))
            }
        }
    }
    companion object {

        const val ISBN_TAG = "canonical_isbn"
        const val TITLE_TAG = "title"
        const val AUTHORS_TAG = "authors"
        const val CATEGORIES_TAG = "categories"
        const val LEXILE_TAG = "lexile"
        const val MIN_AGE_TAG = "min_age"
        const val MAX_AGE_TAG = "max_age"

    }
}