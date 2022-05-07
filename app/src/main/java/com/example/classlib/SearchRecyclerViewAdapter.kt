package com.example.classlib

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class SearchRecyclerViewAdapter (
    private val books: List<Books>
) : RecyclerView.Adapter<SearchRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.book_card, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.titleView.text = books[i].title
        viewHolder.authorView.text = books[i].author
        viewHolder.categoryView.text = books[i].category
        viewHolder.lexileView.text = books[i].lexileLevel
        viewHolder.ageView.text = books[i].age
        viewHolder.nCopiesView.text = books[i].num_copies
        viewHolder.checkedoutView.text = books[i].checked_out
    }

    override fun getItemCount(): Int {
        return books.size
    }

    class ViewHolder internal constructor(itemView: View) :RecyclerView.ViewHolder(itemView) {
        var titleView: TextView
        var authorView: TextView
        var categoryView: TextView
        var lexileView: TextView
        var ageView: TextView
        var nCopiesView: TextView
        var checkedoutView: TextView

        init {
            titleView = itemView.findViewById(R.id.title_view)
            authorView = itemView.findViewById(R.id.author_view)
            categoryView = itemView.findViewById(R.id.category_view)
            lexileView = itemView.findViewById(R.id.lexile_view)
            ageView = itemView.findViewById(R.id.age_view)
            nCopiesView = itemView.findViewById(R.id.ncopies_view)
            checkedoutView = itemView.findViewById(R.id.co_view)
        }
    }
}