package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.R
import com.example.cheongyakhae.databinding.FragmentAnnounceBinding
import com.example.cheongyakhae.adpater.AnnouncementAdapter
import com.example.cheongyakhae.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore

class AnnounceFragment : Fragment() {
    private lateinit var binding: FragmentAnnounceBinding
    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAnnounceBinding.inflate(inflater, container, false)

        // RecyclerView 설정
        adapter = AnnouncementAdapter(announcements) { announcement ->
            // 상세 화면으로 이동
            val bundle = Bundle().apply {
                putString("announcement_title", announcement.announcement_title)
                putString("house_type", announcement.house_type)
                putString("house_detail_type", announcement.house_detail_type)
                putString("announcement_date", announcement.announcement_date)
                putInt("supply_household_count", announcement.supply_household_count ?: 0)
                putString("contact_number", announcement.contact_number)
            }
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, DetailFragment().apply { arguments = bundle })
                .addToBackStack(null)
                .commit()
        }
        binding.announceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announceRecyclerView.adapter = adapter

        fetchAnnouncements()

        return binding.root
    }

    private fun fetchAnnouncements() {
        db.collection("announcements")
            .orderBy("announcement_date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = Announcement(
                        announcement_title = document.getString("announcement_title") ?: "No Title",
                        house_type = document.getString("house_type") ?: "No Type",
                        house_detail_type = document.getString("house_detail_type") ?: "No Detail Type",
                        announcement_date = document.getString("announcement_date") ?: "No Date",
                        supply_household_count = document.getString("supply_household_count")?.toIntOrNull()
                            ?: 0,
                        contact_number = document.getString("contact_number") ?: "No Contact"
                    )
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // 로그 출력 등 에러 처리
            }
    }
}
