package com.example.classlib

//import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SearchResultsActivity : AppCompatActivity(), BooksAdapter.BooksAdapterListener {

    private lateinit var adapter: BooksAdapter
    private lateinit var queries: Array<String>
//    private lateinit var title: String
//    private lateinit var author: String
//    private lateinit var genre: String
//    private var lexileMinLevel: Int = 0
//    private var lexileMaxLevel: Int = 0
//    private var ageMin: Int = 0
//    private var ageMax: Int = 0
//    private lateinit var available: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)

        // Load query values from intent
        val searchIntent = intent
        queries = searchIntent.getStringArrayExtra("queryArray")!!

        // Create a reference to the books collection
        val db = Firebase.firestore
        val booksRef = db.collection("books")

        val query = booksRef
            .whereEqualTo("title", queries[0])
            .whereEqualTo("author", queries[1])
            .whereEqualTo("genre", queries[2])
            .whereGreaterThanOrEqualTo("lexileLevel", queries[3].toInt())
            .whereLessThanOrEqualTo("lexileLevel", queries[4].toInt())
            .whereGreaterThanOrEqualTo("age", queries[5].toInt())
            .whereLessThanOrEqualTo("age", queries[6].toInt())
            .whereEqualTo("available", queries[7])

//        val query: Query = FirebaseFirestore.getInstance().collection("books")

        val recyclerView: RecyclerView = findViewById(R.id.result_recycler)
        adapter = BooksAdapter(query, this)
        recyclerView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onBookSelected(books: Books?) {
        return
    }
}