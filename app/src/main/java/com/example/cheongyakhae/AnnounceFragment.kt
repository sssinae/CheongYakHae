package com.example.cheongyakhae

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cheongyakhae.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore

class AnnounceFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_announce, container, false)

        recyclerView = view.findViewById(R.id.announceRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = AnnouncementAdapter(announcements)
        recyclerView.adapter = adapter

        fetchAnnouncements()

        return view
    }

    private fun fetchAnnouncements() {
        db.collection("announcements")
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = document.toObject(Announcement::class.java)
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AnnounceFragment", "Error fetching data", exception)
            }
    }
}
