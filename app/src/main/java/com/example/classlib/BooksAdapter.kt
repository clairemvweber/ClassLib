package com.example.classlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query

class BooksAdapter(
    query: Query,
    private val listener: BooksAdapterListener
) : FirestoreAdapter<BooksAdapter.BooksViewHolder>(query) {

    class BooksViewHolder(bookView: View) : RecyclerView.ViewHolder(bookView) {

        private val cardView: TextView = bookView.findViewById(R.id.book_card)
        private val title: TextView = bookView.findViewById(R.id.title_view)
        private val author: TextView = bookView.findViewById(R.id.author_view)
        private val genre: TextView = bookView.findViewById(R.id.genre_view)
        private val lexileLevel: TextView = bookView.findViewById(R.id.lexile_view)
        private val age: TextView = bookView.findViewById(R.id.age_view)
        private val available: TextView = bookView.findViewById(R.id.available_view)

        fun bind(snapshot: DocumentSnapshot, listener: BooksAdapterListener) {
            val books: Books? = snapshot.toObject(Books::class.java)
            title.text = books?.title
            author.text = books?.author
            genre.text = books?.genre
            lexileLevel.text = books?.lexileLevel
            age.text = books?.age
            available.text = books?.available

            cardView.setOnClickListener{
                listener.onBookSelected(books)
            }
        }
    }

    interface BooksAdapterListener {
        fun onBookSelected(books: Books?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooksViewHolder {
        return BooksViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.book_card, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        getSnapshot(position)?.let { snapshot ->
            holder.bind(snapshot, listener)
        }
    }
}