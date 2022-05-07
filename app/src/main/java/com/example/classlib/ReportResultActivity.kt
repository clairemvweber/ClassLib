package com.example.classlib

import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.model.Document
import com.google.firebase.ktx.Firebase
import java.util.*

class ReportResultActivity : AppCompatActivity() {
    var userEmail = ""
    val TAG = "ClassLib"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report_result)
        userEmail = intent.getStringExtra("username").toString()
        initializeUI()
        val mRecyclerView = findViewById<RecyclerView>(R.id.list)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        // empty adapter to get rid of the no adapter attached;
        mRecyclerView.adapter =
            MyRecyclerViewAdapter(mutableListOf<Spanned>(), R.layout.activity_report_view)

        val db = Firebase.firestore
        val docRef = db.collection(userEmail)
        val list: MutableList<Spanned> =  mutableListOf<Spanned>()
        // either list em all or only list the ones that have been checked out
        val whichOne = intent.getStringExtra("get").toString() == "checked"
        var sortBy = intent.getStringExtra("sort").toString()
        var sortBy2 = intent.getStringExtra("sort2").toString()
        if(sortBy == "Copies Available"){
            sortBy = "Number of Copies"
        }
        if(sortBy2 == "Copies Available"){
            sortBy2 = "Number of Copies"
        }
        docRef.orderBy(sortBy).get().addOnSuccessListener { document ->
            val temp = document.documents
            val sorted = temp.sortedWith(compareBy({ it.get(sortBy).toString()}, {it.get(sortBy2).toString() }))
            for(a in sorted){
                if(a != null) {
                    if(a.get("Checked Out") == "" && whichOne) {}
                    else {
                        var info = "<big><strong><u>${a.get("Title")}</u></strong></big><br>" +
                                "Author: ${a.get("Author")}<br>Age: ${a.get("Age")}<br>" +
                                "Category: ${a.get("Category")}<br>" +
                                "Lexile Level: ${a.get("Lexile Level")}<br>" +
                                "Number of Copies Available: ${a.get("Number of Copies")}<br>" +
                                "Student(s) who checked out the book: ${a.get("Checked Out")}<br>"
                        var test = Html.fromHtml("$info")
                        list.add(test)
                    }
                    //Log.d(TAG, "DocumentSnapshot data: ${a.get("Title")}")
                }
            }
            if(!list.isEmpty()) {
                //Log.d(TAG, "DocumentSnapshot data: ${list}")

                mRecyclerView.adapter = MyRecyclerViewAdapter(list,R.layout.activity_report_view)
            }
        }.addOnFailureListener { e ->
            Toast.makeText(applicationContext, "Couldn't load data from server",
                Toast.LENGTH_LONG).show()
            Log.w(TAG, "Error reading document", e)
        }
    }

    private fun initializeUI() {
    }

}