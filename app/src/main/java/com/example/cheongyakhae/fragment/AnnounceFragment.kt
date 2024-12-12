package com.example.cheongyakhae.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cheongyakhae.R
import com.example.cheongyakhae.adpater.AnnouncementAdapter
import com.example.cheongyakhae.databinding.FragmentAnnounceBinding
import com.example.cheongyakhae.model.Announcement
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot

class AnnounceFragment : Fragment() {
    private var _binding: FragmentAnnounceBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AnnouncementAdapter
    private val announcements = mutableListOf<Announcement>()
    private val db = FirebaseFirestore.getInstance()

    // 선택된 필터를 저장하는 Map
    private val selectedFilters = mutableMapOf<String, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnnounceBinding.inflate(inflater, container, false)

        setupRecyclerView()
        fetchAnnouncements()
        setupFilterListeners()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter = AnnouncementAdapter(announcements) { announcement ->
            navigateToDetailFragment(announcement)
        }
        binding.announceRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.announceRecyclerView.adapter = adapter
    }

    private fun navigateToDetailFragment(announcement: Announcement) {
        val bundle = Bundle().apply {
            putString("announcement_title", announcement.announcement_title)
            putString("house_type", announcement.house_type)
            putString("house_detail_type", announcement.house_detail_type)
            putString("announcement_date", announcement.announcement_date)
            putString("supply_household_count", announcement.supply_household_count.toString())
            putString("contact_number", announcement.contact_number)
            putString("announcement_url", announcement.announcement_url)
        }
        findNavController().navigate(R.id.action_announceFragment_to_detailFragment, bundle)
    }

    private fun setupFilterListeners() {
        val filters = mapOf(
            binding.regionSeoul to "서울",
            binding.regionBusan to "부산",
            binding.regionDaegu to "대구",
            binding.regionIncheon to "인천",
            binding.regionGwangju to "광주",
            binding.regionDaejeon to "대전",
            binding.regionUlsan to "울산",
            binding.regionSejong to "세종",
            binding.regionGyeonggi to "경기",
            binding.regionGangwon to "강원",
            binding.regionChungbuk to "충북",
            binding.regionChungnam to "충남",
            binding.regionJeonbuk to "전북",
            binding.regionJeonnam to "전남",
            binding.regionGyeongbuk to "경북",
            binding.regionGyeongnam to "경남",
            binding.regionJeju to "제주",
            binding.houseLand to "토지",
            binding.houseSale to "분양주택",
            binding.houseLease to "임대주택",
            binding.houseWelfare to "주거복지",
            binding.houseShop to "상가",
            binding.houseNewlyWed to "신혼희망타운"
        )

        filters.forEach { (view, filter) ->
            view.setOnClickListener {
                if (selectedFilters.values.contains(filter)) {
                    selectedFilters.entries.removeIf { it.value == filter }
                    view.setBackgroundResource(R.drawable.textview_selector)
                } else {
                    selectedFilters[view.id.toString()] = filter
                    view.setBackgroundResource(R.drawable.textview_selector_selected)
                }
                updateSelectedFiltersDisplay()
            }
        }

        binding.applyFilterButton.setOnClickListener {
            fetchFilteredAnnouncements()
        }
    }

    private fun updateSelectedFiltersDisplay() {
        binding.selectedFilters.text =
            "선택된 필터: ${selectedFilters.values.joinToString(" #")}"
    }

    private fun fetchAnnouncements() {
        binding.progressBar.visibility = View.VISIBLE

        db.collection("announcements")
            .orderBy("announcement_date", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = parseDocumentToAnnouncement(document)
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "데이터를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun fetchFilteredAnnouncements() {
        binding.progressBar.visibility = View.VISIBLE

        var query: Query = db.collection("announcements")
            .orderBy("announcement_date", Query.Direction.DESCENDING)

        selectedFilters.values.forEach { filter ->
            query = query.whereArrayContains("filters", filter)
        }

        query.get()
            .addOnSuccessListener { documents ->
                announcements.clear()
                for (document in documents) {
                    val announcement = parseDocumentToAnnouncement(document)
                    announcements.add(announcement)
                }
                adapter.notifyDataSetChanged()
                binding.progressBar.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "필터링된 데이터를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
            }
    }

    private fun parseDocumentToAnnouncement(document: QueryDocumentSnapshot): Announcement {
        val supplyHouseholdCount =
            document.get("supply_household_count")?.toString()?.toIntOrNull() ?: 0
        return Announcement(
            announcement_title = document.getString("announcement_title") ?: "제목 없음",
            house_type = document.getString("house_type") ?: "유형 없음",
            house_detail_type = document.getString("house_detail_type") ?: "세부 유형 없음",
            announcement_date = document.getString("announcement_date") ?: "날짜 없음",
            supply_household_count = supplyHouseholdCount,
            contact_number = document.getString("contact_number") ?: "연락처 없음",
            announcement_url = document.getString("announcement_url") ?: "URL 없음"
        )
    }
}
