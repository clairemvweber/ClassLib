package com.example.classlib

//import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Load user and query values from intent
        val user = intent.getStringExtra("user").toString()
        val queries = intent.getStringArrayExtra("queryArray")!!

        // Create a reference to the books collection
        val db = Firebase.firestore
        val libRef = db.collection(user)

        val list = mutableListOf<Books>()
        var bookList = listOf<Books>()

        val recyclerView = findViewById<RecyclerView>(R.id.search_result_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = SearchRecyclerViewAdapter(bookList)

        // Read contents of snapshot to Book object and store in list
        // Apply availability filter to initial get request
        libRef.whereGreaterThanOrEqualTo("Number of Copies", queries[7])
            .get()
            .addOnSuccessListener { document ->
                val docs = document.documents

                for (d in docs) {
                    if (d != null) {
                        val book = Books()

                        book.title = d.get("Title").toString()
                        book.author = d.get("Author").toString()
                        book.category = d.get("Category").toString()
                        book.lexileLevel = d.get("Lexile Level").toString()
                        book.age = d.get("Age").toString()
                        book.num_copies = d.get("Number of Copies").toString()
                        book.checked_out = d.get("Checked Out").toString()

                        list.add(book)
                    }
                }

                bookList = list.toList()

                // Search functions by query values
                if (queries[0] != "") {
                    bookList = bookList.filter { book ->
                        book.title!!.contains(
                            queries[0],
                            ignoreCase = true
                        )
                    }
                }

                if (queries[1] != "") {
                    bookList = bookList.filter { book ->
                        book.author!!.contains(
                            queries[1],
                            ignoreCase = true
                        )
                    }
                }

                if (queries[2] != "Any") {
                    bookList = bookList.filter { book -> book.category == queries[2] }
                }

                bookList = bookList.filter { book ->
                    if (book.lexileLevel != ""){
                        (queries[3].toInt() <= book.lexileLevel!!.toInt()
                                && book.lexileLevel!!.toInt() <= queries[4].toInt())
                    } else {
                        true
                    }
                }

                bookList = bookList.filter { book ->
                    if (book.age != "") {
                        (queries[5].toInt() <= book.age!!.toInt()
                                && book.age!!.toInt() <= queries[6].toInt())
                    } else {
                        true
                    }
                }

                if (bookList.isNotEmpty()) {
                    Log.i("SearchResultActivity", "list loaded")
                    recyclerView.adapter = SearchRecyclerViewAdapter(bookList)
                }

            }.addOnFailureListener {
                Toast.makeText(
                    applicationContext, "Could load data from server",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
}