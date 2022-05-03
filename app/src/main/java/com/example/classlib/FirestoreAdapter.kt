package com.example.classlib

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener

abstract class FirestoreAdapter<VH: RecyclerView.ViewHolder>(
    private val query: Query
) : RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null
    private val snapshots = ArrayList<DocumentSnapshot>()

    open fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    open fun stopListening() {
        if (registration != null) {
            registration!!.remove()
            registration = null
        }

        snapshots.clear()
    }

    override fun onEvent(
        documentSnapshots: QuerySnapshot?,
        exception: FirebaseFirestoreException?
    ) {
        // Handle errors
        if (exception != null) {
            Log.e("onEvent:error", exception.toString())
            return
        }

        // Dispatch the event
        for (change in documentSnapshots!!.documentChanges) {

            // Snapshot of the changed document
            when (change.type) {

                // New document was added to set of documents matching query
                DocumentChange.Type.ADDED -> onDocumentAdded(change)

                // New document within the query was modified
                DocumentChange.Type.MODIFIED -> onDocumentModified(change)

                // Removed, deleted, no longer matches the query
                DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
            }
        }
    }

    private fun onDocumentAdded(change: DocumentChange?) {
        snapshots.add(change!!.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange?) {
        if (change!!.oldIndex == change.newIndex) {

            // Item changed but remained in same position
            snapshots[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {

            // Item changed and changed position
            snapshots.removeAt(change.oldIndex)
            snapshots.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshots.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun getItemCount(): Int {
        return snapshots.size
    }

    protected open fun getSnapshot(index: Int): DocumentSnapshot? {
        return snapshots[index]
    }
}