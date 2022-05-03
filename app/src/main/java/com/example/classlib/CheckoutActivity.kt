package com.example.classlib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.*
import androidx.core.widget.addTextChangedListener
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class CheckoutActivity : AppCompatActivity() {
    private lateinit var bookTitle: AutoCompleteTextView
    private lateinit var studentID: EditText
    private lateinit var barcodeButton: Button
    private lateinit var returnButton: Button
    private lateinit var checkoutButton: Button
    var userEmail = ""
    val TAG = "ClassLib"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)
        userEmail = intent.getStringExtra("username").toString()
        initializeUI()
        returnButton.setOnClickListener { returnBook() }
        checkoutButton.setOnClickListener { checkOut() }

        // Suggestion text
        val db = Firebase.firestore
        val docRef = db.collection(userEmail)
        val list: MutableList<String> =  mutableListOf<String>()
        docRef.get().addOnSuccessListener { document ->
            val temp = document.documents
            for(a in temp){
                if(a != null) {
                    list.add(a.get("Title").toString())
                    //Log.d(TAG, "DocumentSnapshot data: ${a.get("Title")}")
                }
            }
            if(!list.isEmpty()) {
                ArrayAdapter<String>(this, R.layout.suggestion_layout, list).also { adapter ->
                    bookTitle.setAdapter(adapter)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(applicationContext, "Auto suggestion not available",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun checkOut() {
        val id = studentID.text.toString()
        val db = Firebase.firestore
        val bookTitle = bookTitle.text.toString()
        if((bookTitle != "" && bookTitle != null) || (id != "" && id != null)) {
            val docRef = db.collection(userEmail).document(bookTitle.uppercase())
            docRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    var list = document.get("Checked Out").toString()
                    var booksAvail = Integer.parseInt(
                        document.get("Number of Copies").toString()
                    )
                    // check that there are enough books to be checked out
                    if (booksAvail > 0) {
                        booksAvail -= 1
                        list = if (list == "") {
                            id
                        } else {
                            "$list,$id"
                        }
                        // update the list of ids
                        docRef.update("Checked Out", list).addOnSuccessListener {
                            // update the list of books
                            docRef.update("Number of Copies", booksAvail.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Successfully checked out the book!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // end task
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Unsuccessful at checking out the book",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }.addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Unsuccessful at checking out the book",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                    // show error message, no more books to be checked out
                    else {
                        Toast.makeText(
                            applicationContext,
                            "There are no more ${bookTitle} available",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "This user doesn't have the book: $bookTitle",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
                .addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "This user doesn't have the book: $bookTitle",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }else{
            Toast.makeText(
                applicationContext,
                "Please fill out the fields",
                Toast.LENGTH_LONG
            ).show()
        }


        //Log.d(TAG, numBooks.result.get("Author").toString())
    }

    private fun returnBook() {
        val id = studentID.text.toString()
        val db = Firebase.firestore
        val bookTitle = bookTitle.text.toString()
        if((bookTitle != "" && bookTitle != null) || (id != "" && id != null)) {
            val docRef = db.collection(userEmail).document(bookTitle.uppercase())
            docRef.get().addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")

                    var list = document.get("Checked Out").toString()
                    val newList = list.split(",").toMutableList()
                    if (!newList.contains(id)) {
                        Toast.makeText(
                            applicationContext,
                            "This student ID: $id doesn't have the book: $bookTitle",
                            Toast.LENGTH_LONG).show()
                    } else {
                        newList.remove(id)
                        list = newList.joinToString(",")
                        //Log.d(TAG, list)
                        var booksAvail = Integer.parseInt(
                            document.get("Number of Copies").toString()
                        )
                        booksAvail += 1
                        docRef.update("Checked Out", list).addOnSuccessListener {
                            // update the list of books
                            docRef.update("Number of Copies", booksAvail.toString())
                                .addOnSuccessListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Successfully returned the book!",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    // end task
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Unsuccessful at returning the book",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                        }.addOnFailureListener {
                            Toast.makeText(
                                applicationContext,
                                "Unsuccessful at returning the book",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "This user doesn't have the book: $bookTitle",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
                .addOnFailureListener {
                    Toast.makeText(
                        applicationContext,
                        "This user doesn't have the book: $bookTitle",
                        Toast.LENGTH_LONG
                    ).show()
                }
        }else{
            Toast.makeText(
                applicationContext,
                "Please fill out the fields",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun initializeUI() {
        bookTitle = findViewById(R.id.book_title_return)
        studentID = findViewById(R.id.student_id_return)
        barcodeButton = findViewById(R.id.scan_barcode_return)
        checkoutButton = findViewById(R.id.checkout_book_Button)
        returnButton = findViewById(R.id.return_book)
    }

}