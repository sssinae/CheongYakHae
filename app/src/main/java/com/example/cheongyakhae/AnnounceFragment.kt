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

        fetchAnnouncements()  // Firestore에서 모든 데이터를 한 번에 불러옵니다.

        return view
    }

    private fun fetchAnnouncements() {
        db.collection("announcements")
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = Announcement(
                        announcement_title = document.getString("announcement_title") ?: "No Title",
                        house_type = document.getString("house_type") ?: "No Type",
                        house_detail_type = document.getString("house_detail_type") ?: "No Detail Type",
                        announcement_date = document.getString("announcement_date") ?: "No Date",
                        supply_household_count = document.getString("supply_household_count")?.toIntOrNull() ?: 0,
                        business_name = document.getString("business_name") ?: "Unknown",
                        address = document.getString("address") ?: "No Address",
                        zip_code = document.getString("zip_code") ?: "No Zip",
                        contact_number = document.getString("contact_number") ?: "No Contact",
                        contract_date_start = document.getString("contract_date_start") ?: "No Start Date",
                        contract_date_end = document.getString("contract_date_end") ?: "No End Date",
                        move_in_date = document.getString("move_in_date") ?: "No Move-In Date",
                        announcement_url = document.getString("announcement_url") ?: "No URL",
                        source = document.getString("source") ?: "Unknown"
                    )
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("AnnounceFragment", "Error fetching data", exception)
            }
    }


}
